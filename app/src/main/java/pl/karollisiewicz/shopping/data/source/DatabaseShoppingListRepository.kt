package pl.karollisiewicz.shopping.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import pl.karollisiewicz.shopping.data.source.database.ShoppingListDao
import pl.karollisiewicz.shopping.data.source.database.ShoppingListEntity
import pl.karollisiewicz.shopping.data.source.database.ShoppingListItemDao
import pl.karollisiewicz.shopping.data.source.database.ShoppingListItemEntity
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.domain.ShoppingList.Filter.*
import pl.karollisiewicz.shopping.domain.ShoppingListRepository

class DatabaseShoppingListRepository(
    private val shoppingListDao: ShoppingListDao,
    private val shoppingListItemDao: ShoppingListItemDao,
    private val dispatcher: CoroutineDispatcher
) : ShoppingListRepository {

    override suspend fun findAll(filter: ShoppingList.Filter): List<ShoppingList> =
        withContext(dispatcher) {
            val shoppingLists = when (filter) {
                ALL -> shoppingListDao.getAll()
                ACTIVE -> shoppingListDao.getByActiveness(true)
                ARCHIVED -> shoppingListDao.getByActiveness(false)
            }
            return@withContext shoppingLists.map {
                val shoppingListItems = shoppingListItemDao.getByShoppingList(it.id)
                it.toDomain(shoppingListItems)
            }
        }

    override suspend fun findBy(id: String): ShoppingList? = withContext(dispatcher) {
        val shoppingList = shoppingListDao.getById(id) ?: return@withContext null
        val shoppingListItems = shoppingListItemDao.getByShoppingList(shoppingList.id)
        return@withContext ShoppingList(
            id = shoppingList.id,
            name = shoppingList.name,
            isActive = shoppingList.isActive,
            items = shoppingListItems.map { it.toDomain() }
        )
    }

    override suspend fun save(shoppingList: ShoppingList) {
        withContext(dispatcher) {
            shoppingListDao.insert(shoppingList.toEntity())
            shoppingListItemDao.deleteByShoppingList(shoppingList.id)
            shoppingListItemDao.insert(shoppingList.items.map {
                it.toEntity(shoppingList.id)
            })
        }
    }
}

private fun ShoppingList.toEntity() = ShoppingListEntity(
    id = id,
    name = name,
    isActive = isActive
)

private fun ShoppingList.Item.toEntity(shoppingListId: String) = ShoppingListItemEntity(
    id = id,
    name = name,
    isCompleted = isCompleted,
    shoppingListId = shoppingListId
)

private fun ShoppingListEntity.toDomain(shoppingListItems: List<ShoppingListItemEntity>) =
    ShoppingList(
        id = id,
        name = name,
        isActive = isActive,
        items = shoppingListItems.map { it.toDomain() }
    )

private fun ShoppingListItemEntity.toDomain() = ShoppingList.Item(
    id = id,
    name = name,
    isCompleted = isCompleted
)