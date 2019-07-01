package pl.karollisiewicz.shopping.ui.list

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import kotlinx.android.synthetic.main.fragment_shopping_list.*
import kotlinx.android.synthetic.main.layout_error.*
import org.koin.android.viewmodel.ext.android.viewModel
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.ui.common.hideChildren
import pl.karollisiewicz.shopping.ui.common.show

internal class ShoppingListFragment : Fragment() {
    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private val shoppingListViewModel: ShoppingListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_shopping_list, container, false).also {
            setHasOptionsMenu(true)
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_list_filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.shoppingListFilter -> {
                showFilterPopUpMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun showFilterPopUpMenu() {
        val anchor = activity?.findViewById<View>(R.id.shoppingListFilter) ?: return

        PopupMenu(requireContext(), anchor).run {
            menuInflater.inflate(R.menu.menu_shopping_list_filter, menu)
            setOnMenuItemClickListener {
                true
            }
            show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupFab()
        setupViewModel()
    }

    private fun setupListAdapter() {
        shoppingListAdapter = ShoppingListAdapter { shoppingList ->
            val action = ShoppingListFragmentDirections.actionShoppingListToDetail(
                shoppingList.id,
                resources.getString(R.string.shopping_list_edit)
            )
            findNavController().navigate(action)
        }
        with(shoppingListsRecyclerView) {
            adapter = shoppingListAdapter
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
    }

    private fun setupFab() {
        addShoppingListFab.setOnClickListener {
            val action = ShoppingListFragmentDirections.actionShoppingListToDetail(
                null,
                resources.getString(R.string.shopping_list_new)
            )
            findNavController().navigate(action)
        }
    }

    private fun setupViewModel() {
        with(shoppingListViewModel) {
            viewState.observe(viewLifecycleOwner, Observer { viewState ->
                when (viewState) {
                    is ShoppingListLoading -> showLoadingIndicator()
                    is ShoppingListLoadedEmpty -> showEmptyShoppingList()
                    is ShoppingListLoaded -> showShoppingLists(viewState.shoppingLists)
                    is ShoppingListLoadingError -> showErrorScreen(viewState.errorMessageId)
                }
            })
            loadShoppingLists()
        }
    }

    private fun showLoadingIndicator() {
        shoppingListsContent.hideChildren()
        progressLayout.show()
    }

    private fun showEmptyShoppingList() {
        shoppingListsContent.hideChildren()
        addShoppingListFab.show()
        shoppingListEmptyLayout.show()
    }

    private fun showShoppingLists(shoppingLists: List<ShoppingList>) {
        shoppingListsContent.hideChildren()
        addShoppingListFab.show()
        shoppingListsRecyclerView.show()
        shoppingListAdapter.submitList(shoppingLists)
    }

    private fun showErrorScreen(@StringRes errorMessageId: Int) {
        shoppingListsContent.hideChildren()
        shoppingListErrorLayout.run {
            show()
            setError(errorMessageId)
        }
    }

    private fun View.setError(@StringRes errorMessageId: Int) {
        tvError.text = context.getString(errorMessageId)
    }
}
