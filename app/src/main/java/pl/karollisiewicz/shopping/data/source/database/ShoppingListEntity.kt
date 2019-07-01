package pl.karollisiewicz.shopping.data.source.database

import androidx.room.*

@Entity(tableName = "shopping_lists")
class ShoppingListEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "active") var isActive: Boolean,
    @ColumnInfo(name = "update_date") var updateDate: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "shopping_list_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingListEntity::class,
            parentColumns = ["id"],
            childColumns = ["shopping_list_id"]
        )
    ],
    indices = [Index("shopping_list_id")]
)
class ShoppingListItemEntity(
    @PrimaryKey @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "shopping_list_id")
    var shoppingListId: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "completed")
    var isCompleted: Boolean = false
)