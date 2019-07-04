package pl.karollisiewicz.shopping.ui.list.item

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_shopping_item.view.*
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.ui.common.TextChangedListenerAdapter
import pl.karollisiewicz.shopping.ui.common.clearStrikeThrough
import pl.karollisiewicz.shopping.ui.common.inflate
import pl.karollisiewicz.shopping.ui.common.strikeThrough

typealias ShoppingListItemAdded = (item: ShoppingList.Item) -> Unit
typealias ShoppingListItemRemoved = (item: ShoppingList.Item) -> Unit
typealias ShoppingListItemCompleted = (item: ShoppingList.Item, isCompleted: Boolean) -> Unit
typealias ShoppingListItemRenamed = (item: ShoppingList.Item, newName: String) -> Unit

private const val VIEW_TYPE_SHOPPING_ITEM = 0
private const val VIEW_TYPE_ADD_BUTTON = 1

class ActiveShoppingListItemAdapter(
    private val onShoppingListItemAdded: ShoppingListItemAdded,
    private val onShoppingListItemRemoved: ShoppingListItemRemoved,
    private val onShoppingListItemCompleted: ShoppingListItemCompleted,
    private val onShoppingListItemRenamed: ShoppingListItemRenamed
) : ListAdapter<ShoppingList.Item, RecyclerView.ViewHolder>(
    ShoppingListItemDiff
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            VIEW_TYPE_ADD_BUTTON -> createAddItemViewHolder(parent)
            else -> createShoppingListViewHolder(parent)
        }

    private fun createAddItemViewHolder(parent: ViewGroup): ShoppingListAddItemViewHolder =
        ShoppingListAddItemViewHolder(
            parent.inflate(R.layout.list_item_shopping_item_add),
            onShoppingListItemAdded
        )

    private fun createShoppingListViewHolder(parent: ViewGroup): ShoppingListItemViewHolder =
        ShoppingListItemViewHolder(
            parent.inflate(R.layout.list_item_shopping_item),
            onShoppingListItemRenamed,
            onShoppingListItemCompleted,
            onShoppingListItemRemoved
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SHOPPING_ITEM) {
            (holder as ShoppingListItemViewHolder).bindTo(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        // 1 for additional addItem button at the end of list
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position.isLast) VIEW_TYPE_ADD_BUTTON else VIEW_TYPE_SHOPPING_ITEM
    }

    private val Int.isLast
        get() = this == itemCount - 1
}

class ShoppingListAddItemViewHolder(
    itemView: View,
    onShoppingListItemAdded: ShoppingListItemAdded
) : RecyclerView.ViewHolder(itemView) {
    init {
        itemView.setOnClickListener {
            onShoppingListItemAdded(ShoppingList.Item())
        }
    }
}

private class ShoppingListItemViewHolder(
    itemView: View,
    private val onRenamed: ShoppingListItemRenamed,
    private val onCompleted: ShoppingListItemCompleted,
    private val onRemoved: ShoppingListItemAdded
) : RecyclerView.ViewHolder(itemView) {

    fun bindTo(item: ShoppingList.Item) {
        bindName(item)
        bindCompletedCheckbox(item)
        setOnRemoveClickListener(item)
    }

    private fun bindName(item: ShoppingList.Item) {
        with(itemView.name) {
            setText(item.name)
            addTextChangedListener(TextChangedListenerAdapter {
                onRenamed(item, it.toString())
            })
            if (item.isCompleted) strikeThrough()
            else clearStrikeThrough()
        }
    }

    private fun bindCompletedCheckbox(item: ShoppingList.Item) {
        with(itemView.completed) {
            clearOnCheckedChangeListener()
            isChecked = item.isCompleted
            setOnCheckedChangeListener { _, isChecked ->
                onCompleted(item, isChecked)
            }
        }
    }

    private fun setOnRemoveClickListener(item: ShoppingList.Item) {
        itemView.removeButton.setOnClickListener {
            onRemoved(item)
        }
    }
}

private fun AppCompatCheckBox.clearOnCheckedChangeListener() {
    setOnCheckedChangeListener(null)
}