package pl.karollisiewicz.shopping.data.source.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ShoppingListDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(shoppingList: ShoppingListEntity)

    @Query("SELECT * FROM SHOPPING_LISTS WHERE id = :shoppingListId")
    suspend fun getById(shoppingListId: String): ShoppingListEntity?

    @Query("SELECT * FROM SHOPPING_LISTS")
    suspend fun getAll(): List<ShoppingListEntity>

    @Query("SELECT * FROM SHOPPING_LISTS WHERE active = :isActive")
    suspend fun getByActiveness(isActive: Boolean): List<ShoppingListEntity>
}
