package pl.karollisiewicz.shopping.data.source.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShoppingListEntity::class, ShoppingListItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShoppingListDatabase : RoomDatabase() {
    abstract fun getShoppingListDao(): ShoppingListDao
    abstract fun getShoppingListItemDao(): ShoppingListItemDao
}