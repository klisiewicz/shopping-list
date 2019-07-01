package pl.karollisiewicz.shopping.di

import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karollisiewicz.shopping.data.source.DatabaseShoppingListRepository
import pl.karollisiewicz.shopping.data.source.database.ShoppingListDatabase
import pl.karollisiewicz.shopping.domain.ShoppingListRepository
import pl.karollisiewicz.shopping.ui.detail.ShoppingListDetailViewModel
import pl.karollisiewicz.shopping.ui.list.ShoppingListViewModel

val appModule = module {
    viewModel { ShoppingListViewModel(get()) }
    viewModel { ShoppingListDetailViewModel(get()) }
}

val dataModule = module {
    single { Dispatchers.IO }
    single<ShoppingListRepository> {
        DatabaseShoppingListRepository(get(), get(), get())
    }
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ShoppingListDatabase::class.java,
            "shopping-list.db"
        )
            .build()
    }
    factory {
        get<ShoppingListDatabase>().getShoppingListDao()
    }
    factory {
        get<ShoppingListDatabase>().getShoppingListItemDao()
    }
}