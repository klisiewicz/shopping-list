package pl.karollisiewicz.shopping.ui.list

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import kotlinx.android.synthetic.main.fragment_shopping_list.*
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList

internal class ShoppingListFragment : Fragment() {
    private lateinit var shoppingListAdapter: ShoppingListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_shopping_list, container, false).also {
            setHasOptionsMenu(true)
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_list, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListAdapter()
        setupFab()
    }

    private fun setupListAdapter() {
        shoppingListAdapter = ShoppingListAdapter {
            findNavController().navigate(R.id.actionShoppingListToDetail)
        }
        shoppingListAdapter.submitList(
            listOf(
                ShoppingList(id = "1", name = "Bieronka"),
                ShoppingList(id = "2", name = "Lidl"),
                ShoppingList(id = "2", name = "InterMarche")
            )
        )
        with(shoppingLists) {
            adapter = shoppingListAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, VERTICAL))
        }
    }

    private fun setupFab() {
        addShoppingListFab.setOnClickListener {
            findNavController().navigate(R.id.actionShoppingListToModify)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.filterShoppingLists -> {
                showFilterPopUpMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    private fun showFilterPopUpMenu() {
        val anchor = activity?.findViewById<View>(R.id.filterShoppingLists) ?: return

        PopupMenu(requireContext(), anchor).run {
            menuInflater.inflate(R.menu.menu_shopping_list_filter, menu)
            setOnMenuItemClickListener {
                true
            }
            show()
        }
    }
}
