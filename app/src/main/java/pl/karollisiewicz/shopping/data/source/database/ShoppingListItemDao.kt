package pl.karollisiewicz.shopping.data.source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShoppingListItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shoppingListItems: List<ShoppingListItemEntity>): Array<Long>

    @Query("DELETE FROM shopping_list_items WHERE shopping_list_id = :shoppingListId")
    suspend fun deleteByShoppingList(shoppingListId: String)

    @Query("SELECT * FROM shopping_list_items WHERE shopping_list_id = :shoppingListId")
    suspend fun getByShoppingList(shoppingListId: String): List<ShoppingListItemEntity>
}