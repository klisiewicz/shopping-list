package pl.karollisiewicz.shopping.ui.detail

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.karollisiewicz.shopping.domain.ShoppingList
import pl.karollisiewicz.shopping.domain.ShoppingListRepository
import java.util.*

class ShoppingListDetailViewModel(
    private val shoppingListRepository: ShoppingListRepository
) : ViewModel() {
    private var shoppingList: ShoppingList = ShoppingList(id = UUID.randomUUID().toString())

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
        performAction {
            shoppingList.rename(newName.toString())
        }
    }

    fun archive() {

    }

    fun add(item: ShoppingList.Item) = viewModelScope.launch {
        performAction {
            shoppingList.addItem(item)
        }
    }

    fun rename(item: ShoppingList.Item, newName: String) = viewModelScope.launch {
        if (newName.isEmpty()) {
            performAction {
                shoppingList.removeItem(item)
            }
        } else if (item.name != newName) {
            performAction {
                shoppingList.renameItem(item, newName)
            }
        }
    }

    fun complete(item: ShoppingList.Item) = viewModelScope.launch {
        performAction {
            shoppingList.completeItem(item)
        }
    }

    fun activate(item: ShoppingList.Item) = viewModelScope.launch {
        performAction {
            shoppingList.activate(item)
        }
    }

    fun remove(item: ShoppingList.Item) = viewModelScope.launch {
        performAction {
            shoppingList.removeItem(item)
        }
    }

    private suspend fun performAction(block: () -> ShoppingList) {
        _viewState.value = ShoppingListDetailViewState.Loading
        shoppingList = block()
        shoppingList = shoppingListRepository.save(shoppingList)
        _viewState.value = ShoppingListDetailViewState.Loaded(shoppingList)

    }
}