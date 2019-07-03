package pl.karollisiewicz.shopping.ui.list

import androidx.annotation.StringRes

sealed class ShoppingListViewState

object ShoppingListLoading : ShoppingListViewState()

data class ShoppingListLoaded(val shoppingLists: List<ShoppingListViewEntity>) : ShoppingListViewState()

object ShoppingListLoadedEmpty : ShoppingListViewState()

data class ShoppingListLoadingError(@StringRes val errorMessageId: Int) : ShoppingListViewState()