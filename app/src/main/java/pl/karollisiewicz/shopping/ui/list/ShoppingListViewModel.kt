package pl.karollisiewicz.shopping.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.domain.ShoppingListRepository

class ShoppingListViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {

    private val _viewState = MutableLiveData<ShoppingListViewState>().apply {
        value = ShoppingListLoading
    }
    val viewState: LiveData<ShoppingListViewState> = _viewState

    fun loadShoppingLists(filter: ShoppingList.Filter = ShoppingList.Filter.ALL) {
        viewModelScope.launch {
            try {
                tryToLoadShoppingLists()
            } catch (e: Exception) {
                notifyLoadingFailed()
            }
        }
    }

    private suspend fun tryToLoadShoppingLists() {
        val shoppingLists = shoppingListRepository.findAll()
        _viewState.value =
            if (shoppingLists.isNotEmpty()) ShoppingListLoaded(shoppingLists.map {
                it.toViewEntity()
            })
            else ShoppingListLoadedEmpty
    }

    private fun notifyLoadingFailed() {
        _viewState.value = ShoppingListLoadingError(R.string.unknown_error)
    }
}

private fun ShoppingList.toViewEntity() =
    ShoppingListViewEntity(
        id = id.toString(),
        name = name,
        activeItems = items.filter { it.isNotCompleted }.joinToString(separator = ", ") { it.name },
        numberOfCompletedItems = items.count { it.isCompleted }.toString()
    )
