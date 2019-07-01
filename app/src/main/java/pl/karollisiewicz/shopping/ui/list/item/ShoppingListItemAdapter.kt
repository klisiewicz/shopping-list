package pl.karollisiewicz.shopping.ui.list.item

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_shopping_item.view.*
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.ui.common.inflate

typealias ShoppingListItemAdded = (item: ShoppingList.Item) -> Unit
typealias ShoppingListItemRemoved = (item: ShoppingList.Item) -> Unit
typealias ShoppingListItemCompleted = (item: ShoppingList.Item, isCompleted: Boolean) -> Unit
typealias ShoppingListItemRenamed = (item: ShoppingList.Item, newName: String) -> Unit

private const val VIEW_TYPE_SHOPPING_ITEM = 0
private const val VIEW_TYPE_ADD_BUTTON = 1

class ShoppingListItemAdapter(
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
        with(itemView) {
            bindName(item)
            bindCompletedCheckbox(item)
            setOnRemoveClickListener(item)
        }
    }

    private fun View.bindName(item: ShoppingList.Item) {
        name.setText(item.name)
        name.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                onRenamed(item, name.text.toString())
        }
        if (item.isCompleted) name.strikeThrough()
        else name.clearStrikeThrough()
        if (item.name.isEmpty()) name.requestFocusAndShowKeyboard()
    }

    private fun View.bindCompletedCheckbox(item: ShoppingList.Item) {
        completed.clearOnCheckedChangeListener()
        completed.isChecked = item.isCompleted
        completed.setOnCheckedChangeListener { _, isChecked ->
            onCompleted(item, isChecked)
        }
    }

    private fun View.setOnRemoveClickListener(item: ShoppingList.Item) {
        removeButton.setOnClickListener {
            onRemoved(item)
        }
    }
}

private fun AppCompatEditText.requestFocusAndShowKeyboard() {
    requestFocus()
    getSystemService(context, InputMethodManager::class.java)?.showSoftInput(
        this,
        InputMethodManager.SHOW_IMPLICIT
    )
}

private fun AppCompatEditText.strikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

private fun AppCompatEditText.clearStrikeThrough() {
    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
}

private fun AppCompatCheckBox.clearOnCheckedChangeListener() {
    setOnCheckedChangeListener(null)
}