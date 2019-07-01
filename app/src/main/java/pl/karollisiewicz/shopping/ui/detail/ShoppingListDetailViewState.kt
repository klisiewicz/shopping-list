package pl.karollisiewicz.shopping.ui.detail

import pl.karollisiewicz.shopping.domain.ShoppingList

sealed class ShoppingListDetailViewState {
    object Loading : ShoppingListDetailViewState()

    class Loaded(val shoppingList: ShoppingList) : ShoppingListDetailViewState()
}
