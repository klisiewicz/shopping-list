package pl.karollisiewicz.shopping.domain

import java.util.*

data class ShoppingList(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val isActive: Boolean = true,
    val items: List<Item> = emptyList()
) {
    val isNotActive: Boolean = !isActive

    fun rename(newName: String): ShoppingList = copy(name = newName)

    fun archive(): ShoppingList =
        copy(isActive = false, items = items.map { it.copy(isCompleted = true) })

    fun addItem(item: Item): ShoppingList = copy(items = items.toMutableList().apply { add(item) })

    fun removeItem(item: Item): ShoppingList =
        copy(items = items.toMutableList().apply { remove(item) })

    fun activateItem(item: Item): ShoppingList {
        val activeItem = item.copy(isCompleted = false)
        return copy(items = items.replace(activeItem))
    }

    fun completeItem(item: Item): ShoppingList {
        val completedItem = item.copy(isCompleted = true)
        return copy(items = items.replace(completedItem))
    }

    fun renameItem(item: Item, newName: String): ShoppingList {
        val renamedItem = item.copy(name = newName)
        return copy(items = items.replace(renamedItem))
    }

    fun isNotEmpty(): Boolean = name.isNotEmpty() && items.isNotEmpty()

    data class Item(
        val id: String = UUID.randomUUID().toString(),
        val name: String = "",
        val isCompleted: Boolean = false
    ) {
        val isNotCompleted: Boolean = !isCompleted
    }

    enum class Filter {
        ACTIVE, ARCHIVED, ALL
    }
}

private fun List<ShoppingList.Item>.replace(item: ShoppingList.Item) = map {
    if (it.id == item.id) item else it
}
