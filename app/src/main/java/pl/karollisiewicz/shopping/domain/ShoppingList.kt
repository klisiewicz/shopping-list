package pl.karollisiewicz.shopping.domain

data class ShoppingList(
    val id: String,
    val name: String,
    private val items: List<Item> = emptyList()
) {

    class Item(val name: String, val isCompleted: Boolean)
}