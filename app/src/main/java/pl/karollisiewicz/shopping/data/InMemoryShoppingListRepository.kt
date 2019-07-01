package pl.karollisiewicz.shopping.data

import kotlinx.coroutines.delay
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.domain.ShoppingListRepository
import java.util.*

class InMemoryShoppingListRepository : ShoppingListRepository {
    private val shoppingLists = mutableMapOf<String, ShoppingList>(
//        "1" to ShoppingList(
//            id = "1",
//            name = "Bieronka",
//            items = listOf(
//                ShoppingList.Item(id = "1", name = "cebula"),
//                ShoppingList.Item(id = "2", name = "pomidory", isCompleted = true)
//            )
//        ),
//        "2" to ShoppingList(
//            id = "2",
//            name = "Lidl",
//            items = listOf(
//                ShoppingList.Item(id = "3", name = "rzodkiewka")
//            )
//        ),
//        "3" to ShoppingList(
//            id = "3",
//            name = "Inter Marche",
//            items = listOf(
//                ShoppingList.Item(id = "4", name = "ryba"),
//                ShoppingList.Item(id = "5", name = "miód", isCompleted = true),
//                ShoppingList.Item(id = "6", name = "orzechy")
//            )
//        )
    )

    override suspend fun save(shoppingList: ShoppingList): ShoppingList {
        return if (shoppingList.id == null) {
            val newId = UUID.randomUUID().toString()
            val updatedShoppingList = shoppingList.copy(id = newId)
            shoppingLists[newId] = updatedShoppingList
            updatedShoppingList
        } else {
            shoppingLists[shoppingList.id] = shoppingList
            shoppingList
        }

    }

    override suspend fun findAll(): List<ShoppingList> {
        delay(500)
        return shoppingLists.values.toList()
    }

    override suspend fun findBy(id: String): ShoppingList? {
        delay(250)
        return shoppingLists[id]
    }
}