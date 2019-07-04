package pl.karollisiewicz.shopping.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.domain.ShoppingListRepository

class ShoppingListDetailViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    private var shoppingList: ShoppingList = ShoppingList()

    private val _viewState = MutableLiveData<ShoppingListDetailViewState>().apply {
        value = ShoppingListDetailViewState.Loading
    }

    val viewState: LiveData<ShoppingListDetailViewState> = _viewState

    fun loadShoppingList(shoppingListId: String?) {
        viewModelScope.launch {
            if (shoppingListId != null) {
                shoppingList = shoppingListRepository.findBy(shoppingListId) ?: ShoppingList()
            }
            _viewState.value = ShoppingListDetailViewState.Loaded(shoppingList)
        }
    }

    fun rename(newName: String) {
        if (shoppingList.name != newName)
            perform { shoppingList.rename(newName) }
    }

    fun archive() = viewModelScope.launch {
        if (shoppingList.isActive) {
            performAndUpdate { shoppingList.archive() }
            save()
        }
    }

    fun save() = viewModelScope.launch {
        if (shoppingList.isNotEmpty()) {
            shoppingList = shoppingList.clearEmptyItems()
            shoppingListRepository.save(shoppingList)
        }
    }

    fun add(item: ShoppingList.Item) {
        performAndUpdate { shoppingList.addItem(item) }
    }

    fun rename(item: ShoppingList.Item, newName: String) {
        if (newName.isEmpty())
            performAndUpdate { shoppingList.removeItem(item) }
        else if (item.name != newName)
            perform { shoppingList.renameItem(item, newName) }
    }

    fun complete(item: ShoppingList.Item) = viewModelScope.launch {
        performAndUpdate { shoppingList.completeItem(item) }
    }

    fun activate(item: ShoppingList.Item) {
        performAndUpdate { shoppingList.activateItem(item) }
    }

    fun remove(item: ShoppingList.Item) {
        performAndUpdate { shoppingList.removeItem(item) }
    }

    private fun perform(block: () -> ShoppingList) {
        shoppingList = block()
    }

    private fun performAndUpdate(block: () -> ShoppingList) {
        shoppingList = block()
        _viewState.value = ShoppingListDetailViewState.Loaded(shoppingList)
    }
}