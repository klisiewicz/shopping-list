package pl.karollisiewicz.shopping.domain

import pl.karollisiewicz.shopping.domain.ShoppingList.Filter.ALL

interface ShoppingListRepository {
    suspend fun findAll(filter: ShoppingList.Filter = ALL): List<ShoppingList>

    suspend fun findBy(id: String): ShoppingList?

    suspend fun save(shoppingList: ShoppingList)
}