package pl.karollisiewicz.shopping.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.amshove.kluent.shouldEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.karollisiewicz.shopping.R
import pl.karollisiewicz.shopping.domain.ShoppingList.Filter.*
import pl.karollisiewicz.shopping.domain.activeWithItems
import pl.karollisiewicz.shopping.domain.completedWithNoItems
import pl.karollisiewicz.shopping.domain.data.source.FakeShoppingListRepository
import pl.karollisiewicz.shopping.util.MainCoroutineRule
import pl.karollisiewicz.shopping.util.getLastValue

@ExperimentalCoroutinesApi
class ShoppingListViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var objectUnderTest: ShoppingListViewModel
    private lateinit var shoppingListRepository: FakeShoppingListRepository

    @Before
    fun beforeEach() {
        shoppingListRepository = FakeShoppingListRepository()
        objectUnderTest = ShoppingListViewModel(shoppingListRepository)
    }

    @Test
    fun `should emit loading state when fetching data`() {
        mainCoroutineRule.pauseDispatcher()

        objectUnderTest.loadShoppingLists()

        objectUnderTest.viewState.getLastValue() shouldEqual ShoppingListLoading
    }

    @Test
    fun `should emit loaded empty list when there are no shopping lists`() {
        with(objectUnderTest) {
            loadShoppingLists()
            viewState.getLastValue() shouldEqual ShoppingListLoadedEmpty
        }
    }

    @Test
    fun `should emit loaded state with archived shopping lists`() {
        shoppingListRepository.addShoppingLists(completedWithNoItems, activeWithItems)

        with(objectUnderTest) {
            filter = ARCHIVED
            loadShoppingLists()

            viewState.getLastValue() shouldEqual ShoppingListLoaded(
                listOf(
                    completedEmptyListViewEntity
                )
            )
        }
    }

    @Test
    fun `should emit loaded state with active shopping lists`() {
        shoppingListRepository.addShoppingLists(completedWithNoItems, activeWithItems)

        with(objectUnderTest) {
            filter = ACTIVE
            loadShoppingLists()

            viewState.getLastValue() shouldEqual ShoppingListLoaded(
                listOf(activeListViewEntity)
            )
        }
    }

    @Test
    fun `should emit loading state, then loaded state with all shopping lists`() {
        shoppingListRepository.addShoppingLists(completedWithNoItems, activeWithItems)

        mainCoroutineRule.pauseDispatcher()

        with(objectUnderTest) {
            filter = ALL
            loadShoppingLists()
            viewState.getLastValue() shouldEqual ShoppingListLoading
        }

        mainCoroutineRule.resumeDispatcher()

        with(objectUnderTest) {
            viewState.getLastValue() shouldEqual ShoppingListLoaded(
                listOf(completedEmptyListViewEntity, activeListViewEntity)
            )
        }
    }

    @Test
    fun `should emit error state when fetching fails`() {
        shoppingListRepository.emitError = true

        with(objectUnderTest) {
            loadShoppingLists()
            viewState.getLastValue() shouldEqual ShoppingListLoadingError(R.string.unknown_error)
        }
    }
}

private val activeListViewEntity = activeWithItems.run {
    ShoppingListViewEntity(id, name, items.first().name, "1")
}

private val completedEmptyListViewEntity = completedWithNoItems.run {
    ShoppingListViewEntity(id, name, "", "0")
}
