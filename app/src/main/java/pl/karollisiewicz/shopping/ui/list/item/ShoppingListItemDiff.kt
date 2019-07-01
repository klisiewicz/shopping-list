package pl.karollisiewicz.shopping.ui.list.item

import androidx.recyclerview.widget.DiffUtil
import pl.karollisiewicz.shopping.domain.ShoppingList

object ShoppingListItemDiff : DiffUtil.ItemCallback<ShoppingList.Item>() {
    override fun areItemsTheSame(oldItem: ShoppingList.Item, newItem: ShoppingList.Item) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ShoppingList.Item, newItem: ShoppingList.Item) =
        oldItem == newItem
}