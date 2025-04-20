package com.skipper.taskManager.ui.screens.home.components

import androidx.activity.ComponentActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.skipper.taskManager.data.db.TaskDao
import com.skipper.taskManager.data.model.Priority
import com.skipper.taskManager.data.model.Task
import com.skipper.taskManager.data.repo.TaskRepository
import com.skipper.taskManager.ui.screens.home.HomeScreen
import com.skipper.taskManager.ui.viewModel.TaskViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test


class HomeScreenUITest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testFilterAndSortOnHomeScreen() {
        composeTestRule.setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val fakeViewModel = FakeTaskViewModel()
                HomeScreenTestWrapper(fakeViewModel, navController)
            }
        }

        // Open filter menu
        composeTestRule.onNodeWithContentDescription("Filter").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Completed").assertIsDisplayed().performClick()

        // Check if only completed task is shown
        composeTestRule.onNodeWithText("B Task").assertIsDisplayed()
        composeTestRule.onNodeWithText("A Task").assertDoesNotExist()
        composeTestRule.onNodeWithText("C Task").assertDoesNotExist()

        // Open sort menu
        composeTestRule.onNodeWithContentDescription("Sort").assertIsDisplayed().performClick()
        composeTestRule.onNodeWithText("Alphabetically").assertIsDisplayed().performClick()

        // Now check ordering if needed
    }
}

@Composable
fun HomeScreenTestWrapper(viewModel: TaskViewModel, navController: NavController) {
    // Optional: simulate state collection or inject directly
    HomeScreen(navController = navController, viewModel = viewModel)
}





class FakeTaskViewModel : TaskViewModel(FakeTaskRepository()) {
    private val _tasks = MutableStateFlow(
        listOf(
            Task(id = 1, title = "A Task", isCompleted = false, priority = Priority.MEDIUM),
            Task(id = 2, title = "B Task", isCompleted = true, priority = Priority.HIGH)
        )
    )

    override val tasks: StateFlow<List<Task>> = _tasks
}

class FakeTaskViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FakeTaskViewModel() as T
    }
}

// Fake implementation of TaskDao for testing
class FakeTaskDao : TaskDao {
    private val tasks = mutableListOf<Task>(
        Task(id = 1, title = "A Task", isCompleted = false, priority = Priority.MEDIUM),
        Task(id = 2, title = "B Task", isCompleted = true, priority = Priority.HIGH)
    )

    // Simulate Flow of tasks
    override fun getAllTasks(): Flow<List<Task>> = flow {
        emit(tasks)
    }

    override suspend fun insert(task: Task) {
        tasks.add(task)
    }

    override suspend fun delete(task: Task) {
        tasks.remove(task)
    }

    override suspend fun update(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index != -1) {
            tasks[index] = task
        }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return tasks.find { it.id == id }
    }

    override suspend fun clearAll() {

    }
}

// Fake TaskRepository using FakeTaskDao


class FakeTaskRepository : TaskRepository(FakeTaskDao()) {
    private val _tasks = MutableStateFlow(
        listOf(
            Task(id = 1, title = "A Task", isCompleted = false, priority =  Priority.MEDIUM),
            Task(id = 2, title = "B Task", isCompleted = true, priority =  Priority.HIGH)
        )
    )

    override val allTasks: Flow<List<Task>> = _tasks

    override suspend fun insert(task: Task) {
        _tasks.value = _tasks.value + task
    }

    override suspend fun delete(task: Task) {
        _tasks.value = _tasks.value - task
    }

    override suspend fun update(task: Task) {
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) task else it
        }
    }

    override suspend fun getTask(id: Long): Task? = _tasks.value.find { it.id == id }
}
