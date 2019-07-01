package pl.karollisiewicz.shopping.domain

interface ShoppingListRepository {
    suspend fun findAll(): List<ShoppingList>

    suspend fun findBy(id: String): ShoppingList?

    suspend fun save(shoppingList: ShoppingList): ShoppingList
}