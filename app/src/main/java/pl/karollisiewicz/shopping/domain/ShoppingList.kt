package pl.karollisiewicz.shopping.domain

import java.util.*

data class ShoppingList(
    val id: String? = null,
    val name: String = "",
    val isActive: Boolean = true,
    val items: List<Item> = emptyList()
) {
    fun rename(newName: String): ShoppingList = copy(name = newName)

    fun archive(): ShoppingList = copy(isActive = false)

    fun activate(item: Item): ShoppingList {
        val activeItem = item.copy(isCompleted = false)
        return copy(items = items.replace(activeItem))
    }

    fun addItem(item: Item): ShoppingList = copy(items = items.toMutableList().apply { add(item) })

    fun removeItem(item: Item): ShoppingList =
        copy(items = items.toMutableList().apply { remove(item) })

    fun completeItem(item: Item): ShoppingList {
        val completedItem = item.copy(isCompleted = true)
        return copy(items = items.replace(completedItem))
    }

    fun renameItem(item: Item, newName: String): ShoppingList {
        val renamedItem = item.copy(name = newName)
        return copy(items = items.replace(renamedItem))
    }

    data class Item(
        val id: String? = UUID.randomUUID().toString(),
        val name: String = "",
        val isCompleted: Boolean = false
    ) {
        val isNotEmpty: Boolean = name.isNotEmpty()
    }

    enum class Filter {
        ACTIVE, ARCHIVED, ALL
    }
}

private fun List<ShoppingList.Item>.replace(item: ShoppingList.Item) = map {
    if (it.id == item.id) item else it
}
