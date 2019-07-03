package pl.karollisiewicz.shopping.domain.data.source

import androidx.annotation.VisibleForTesting
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.domain.ShoppingListRepository

class FakeShoppingListRepository : ShoppingListRepository {
    @VisibleForTesting
    var emitError = false

    private val shoppingLists = mutableMapOf<String, ShoppingList>()

    override suspend fun findAll(filter: ShoppingList.Filter): List<ShoppingList> {
        if (emitError) throw Exception()
        return when (filter) {
            ShoppingList.Filter.ALL -> shoppingLists.values.toList()
            ShoppingList.Filter.ACTIVE -> shoppingLists.values.filter { it.isActive }
            ShoppingList.Filter.ARCHIVED -> shoppingLists.values.filter { it.isNotActive }
        }
    }

    override suspend fun findBy(id: String): ShoppingList? {
        if (emitError) throw Exception()
        return shoppingLists[id]
    }

    override suspend fun save(shoppingList: ShoppingList) {
        if (emitError) throw Exception()
        shoppingLists[shoppingList.id] = shoppingList
    }

    @VisibleForTesting
    fun addShoppingLists(vararg shoppingLists: ShoppingList) {
        for (shoppingList in shoppingLists) {
            this.shoppingLists[shoppingList.id] = shoppingList
        }
    }
}