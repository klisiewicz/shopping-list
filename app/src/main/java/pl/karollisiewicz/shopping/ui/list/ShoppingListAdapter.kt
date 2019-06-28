package pl.karollisiewicz.shopping.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_shopping_list.view.*
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList

class ShoppingListAdapter(
    private val shoppingListSelectionListener: ((ShoppingList) -> Unit) = {}
) :
    ListAdapter<ShoppingList, ShoppingListAdapter.ViewHolder>(RepoDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflateView(), shoppingListSelectionListener)

    private fun ViewGroup.inflateView(): View =
        LayoutInflater.from(context).inflate(R.layout.list_item_shopping_list, this, false)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    class ViewHolder(
        itemView: View,
        private val shoppingListSelectionListener: ((ShoppingList) -> Unit)
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bindTo(shoppingList: ShoppingList) {
            with(itemView) {
                name.text = shoppingList.name
                setOnClickListener {
                    shoppingListSelectionListener(shoppingList)
                }
            }
        }
    }

    private class RepoDiff : DiffUtil.ItemCallback<ShoppingList>() {
        override fun areItemsTheSame(oldItem: ShoppingList, newItem: ShoppingList) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShoppingList, newItem: ShoppingList) =
            oldItem == newItem
    }
}