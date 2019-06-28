package pl.karollisiewicz.shopping.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_shopping_list_detail.*
import pl.karollisiewicz.shopping.R

class ShoppingListDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_shopping_list_detail, container, false).also {
            setHasOptionsMenu(true)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupFab()
    }

    private fun setupFab() {
        editShoppingListFab.setOnClickListener {
            findNavController().navigate(R.id.actionShoppingListDetailToModify)
        }
    }
}
