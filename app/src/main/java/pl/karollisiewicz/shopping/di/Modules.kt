package pl.karollisiewicz.shopping.di

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.karollisiewicz.shopping.data.InMemoryShoppingListRepository
import pl.karollisiewicz.shopping.domain.ShoppingListRepository
import pl.karollisiewicz.shopping.ui.detail.ShoppingListDetailViewModel
import pl.karollisiewicz.shopping.ui.list.ShoppingListViewModel

val appModule = module {
    single<ShoppingListRepository> { InMemoryShoppingListRepository() }
    viewModel { ShoppingListViewModel(get()) }
    viewModel { ShoppingListDetailViewModel(get()) }
}