package pl.karollisiewicz.shopping.ui.list

data class ShoppingListViewEntity(
    val id: String,
    val name: String,
    val activeItems: String,
    val numberOfCompletedItems: String
)
