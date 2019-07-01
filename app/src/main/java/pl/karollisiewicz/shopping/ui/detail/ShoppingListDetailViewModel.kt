package pl.karollisiewicz.shopping.ui.detail

import android.text.Editable
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
                shoppingList = shoppingListRepository.findBy(shoppingListId)!!
            }
            _viewState.value = ShoppingListDetailViewState.Loaded(shoppingList)
        }

    }

    fun rename(newName: Editable?) = viewModelScope.launch {
        performAndSave {
            shoppingList.rename(newName.toString())
        }
    }

    fun archive() {

    }

    fun add(item: ShoppingList.Item) = viewModelScope.launch {
        performAndSave {
            shoppingList.addItem(item)
        }
    }

    fun rename(item: ShoppingList.Item, newName: String) = viewModelScope.launch {
        if (newName.isEmpty()) {
            performAndSave {
                shoppingList.removeItem(item)
            }
        } else if (item.name != newName) {
            performAndSave {
                shoppingList.renameItem(item, newName)
            }
        }
    }

    fun complete(item: ShoppingList.Item) = viewModelScope.launch {
        performAndSave {
            shoppingList.completeItem(item)
        }
    }

    fun activate(item: ShoppingList.Item) = viewModelScope.launch {
        performAndSave {
            shoppingList.activate(item)
        }
    }

    fun remove(item: ShoppingList.Item) = viewModelScope.launch {
        performAndSave {
            shoppingList.removeItem(item)
        }
    }

    private suspend fun performAndSave(block: () -> ShoppingList) {
        _viewState.value = ShoppingListDetailViewState.Loading
        shoppingList = block()
        _viewState.value = ShoppingListDetailViewState.Loaded(shoppingList)
        shoppingListRepository.save(shoppingList)
    }
}