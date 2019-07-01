package pl.karollisiewicz.shopping.ui.list

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_shopping_list.view.*
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.ui.common.inflate

class ShoppingListAdapter(
    private val shoppingListSelectionListener: ((ShoppingListViewEntity) -> Unit) = {}
) :
    ListAdapter<ShoppingListViewEntity, ShoppingListAdapter.ViewHolder>(ShoppingListDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            parent.inflate(R.layout.list_item_shopping_list),
            shoppingListSelectionListener
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindTo(getItem(position))

    class ViewHolder(
        itemView: View,
        private val shoppingListSelectionListener: ((ShoppingListViewEntity) -> Unit)
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bindTo(shoppingList: ShoppingListViewEntity) {
            with(itemView) {
                shoppingListName.text = shoppingList.name
                shoppingListActiveItems.text = shoppingList.getActiveItemsText()
                setOnClickListener {
                    shoppingListSelectionListener(shoppingList)
                }
            }
        }

        private fun ShoppingListViewEntity.getActiveItemsText() =
            if (activeItems.isNotEmpty()) activeItems
            else itemView.resources.getString(R.string.shopping_list_item_no_active)
    }

    private object ShoppingListDiff : DiffUtil.ItemCallback<ShoppingListViewEntity>() {
        override fun areItemsTheSame(
            oldItem: ShoppingListViewEntity,
            newItem: ShoppingListViewEntity
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ShoppingListViewEntity,
            newItem: ShoppingListViewEntity
        ) = oldItem == newItem
    }
}