package pl.karollisiewicz.shopping.ui.list

import androidx.annotation.StringRes
import pl.karollisiewicz.shopping.domain.ShoppingList

sealed class ShoppingListViewState

object ShoppingListLoading : ShoppingListViewState()

class ShoppingListLoaded(val shoppingLists: List<ShoppingList>) : ShoppingListViewState()

object ShoppingListLoadedEmpty : ShoppingListViewState()

class ShoppingListLoadingError(@StringRes val errorMessageId: Int) : ShoppingListViewState()