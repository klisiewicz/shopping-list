package pl.karollisiewicz.shopping.domain

val completedWithNoItems = ShoppingList(
    id = "1",
    name = "completedWithNoItems",
    isActive = false,
    items = emptyList()
)

val activeWithItems = ShoppingList(
    id = "2",
    name = "activeWithItems",
    isActive = true,
    items = listOf(
        ShoppingList.Item(
            id = "1",
            name = "item-1",
            isCompleted = false
        ),
        ShoppingList.Item(
            id = "2",
            name = "item-2",
            isCompleted = true
        )
    )
)