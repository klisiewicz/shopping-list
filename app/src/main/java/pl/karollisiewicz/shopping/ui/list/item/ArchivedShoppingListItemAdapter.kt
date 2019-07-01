package pl.karollisiewicz.shopping.ui.list.item

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_shopping_item_archived.view.*
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.ui.common.inflate

class ArchivedShoppingListItemAdapter : ListAdapter<ShoppingList.Item, RecyclerView.ViewHolder>(
    ShoppingListItemDiff
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ArchivedViewHolder(parent.inflate(R.layout.list_item_shopping_item_archived))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ArchivedViewHolder).bindTo(getItem(position))
    }

    class ArchivedViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(item: ShoppingList.Item) {
            with(itemView) {
                name.text = item.name
                name.strikeThrough()
                completed.isChecked = item.isCompleted
            }
        }
    }
}

fun AppCompatTextView.strikeThrough() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}