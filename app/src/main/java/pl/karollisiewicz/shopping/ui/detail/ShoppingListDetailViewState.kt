package pl.karollisiewicz.shopping.ui.detail

import androidx.annotation.StringRes
import pl.karollisiewicz.shopping.domain.ShoppingList

sealed class ShoppingListDetailViewState {
    object Loading : ShoppingListDetailViewState()

    class Loaded(val shoppingList: ShoppingList) : ShoppingListDetailViewState()

    class Error(@StringRes val errorMessageId: Int) : ShoppingListDetailViewState()
}
