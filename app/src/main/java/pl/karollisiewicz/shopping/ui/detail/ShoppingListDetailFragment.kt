package pl.karollisiewicz.shopping.ui.detail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_shopping_list_detail.*
import kotlinx.android.synthetic.main.layout_shopping_list_detail.*
import org.koin.android.viewmodel.ext.android.viewModel
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.ui.common.hideChildren
import pl.karollisiewicz.shopping.ui.common.show
import pl.karollisiewicz.shopping.ui.list.item.ActiveShoppingListItemAdapter
import pl.karollisiewicz.shopping.ui.list.item.ArchivedShoppingListItemAdapter

class ShoppingListDetailFragment : Fragment() {
    private val shoppingListViewModel: ShoppingListDetailViewModel by viewModel()
    private lateinit var shoppingListItemAdapter: ListAdapter<ShoppingList.Item, RecyclerView.ViewHolder>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_shopping_list_detail, container, false).also {
            setHasOptionsMenu(true)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        with(shoppingListViewModel) {
            viewState.observe(viewLifecycleOwner, Observer { viewState ->
                when (viewState) {
                    is ShoppingListDetailViewState.Loading -> showLoadingIndicator()
                    is ShoppingListDetailViewState.Loaded -> showShoppingList(viewState.shoppingList)
                }
            })
            loadShoppingList(arguments?.shoppingListId)
        }
    }

    private val Bundle.shoppingListId: String?
        get() {
            return ShoppingListDetailFragmentArgs.fromBundle(this).shoppingListId
        }

    private fun showLoadingIndicator() {
        shoppingListDetailContent.hideChildren()
        progressLayout.show()
    }

    private fun showShoppingList(shoppingList: ShoppingList) {
        shoppingListDetailContent.hideChildren()
        shoppingListName.setText(shoppingList.name)
        shoppingListName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                shoppingListViewModel.rename(shoppingListName.text)
            }
        }
        shoppingListName.isEnabled = shoppingList.isActive
        shoppingListItemAdapter = createListAdapter(shoppingList)
        shoppingListItemAdapter.submitList(shoppingList.items)
        shoppingListItemRecyclerView.adapter = shoppingListItemAdapter
        shoppingListDetail.show()
    }

    private fun createListAdapter(shoppingList: ShoppingList): ListAdapter<ShoppingList.Item, RecyclerView.ViewHolder> {
        return if (shoppingList.isActive) ActiveShoppingListItemAdapter(
            onShoppingListItemRenamed = { item: ShoppingList.Item, newName: String ->
                shoppingListViewModel.rename(item, newName)
            },
            onShoppingListItemCompleted = { item: ShoppingList.Item, isCompleted: Boolean ->
                if (isCompleted) shoppingListViewModel.complete(item)
                else shoppingListViewModel.activate(item)
            },
            onShoppingListItemAdded = { item -> shoppingListViewModel.add(item) },
            onShoppingListItemRemoved = { item -> shoppingListViewModel.remove(item) }
        )
        else return ArchivedShoppingListItemAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_list_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.shoppingListArchive -> {
                shoppingListViewModel.archive()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
