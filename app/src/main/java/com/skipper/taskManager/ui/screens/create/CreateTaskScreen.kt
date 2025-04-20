package com.skipper.taskManager.ui.screens.create

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.skipper.taskManager.R
import com.skipper.taskManager.data.model.Priority
import com.skipper.taskManager.data.model.Task
import com.skipper.taskManager.di.AppModule
import com.skipper.taskManager.ui.viewModel.TaskViewModel
import com.skipper.taskManager.ui.viewModel.TaskViewModelFactory
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(navController: NavController) {
    val viewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(AppModule.repository))

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var dueDateMillis by remember { mutableStateOf<Long?>(null) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(year, month, dayOfMonth)
                dueDateMillis = calendar.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.create_task)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title*") },
                modifier = Modifier.fillMaxWidth(),
                isError = title.isBlank()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            PriorityDropdown(priority) { selected ->
                priority = selected
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = { datePickerDialog.show() }) {
                Text(
                    text = if (dueDateMillis != null)
                        stringResource(
                            R.string.due_date,
                            Date(dueDateMillis!!).toLocaleString().substring(0, 11)
                        )
                    else stringResource(R.string.pick_due_date)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        viewModel.addTask(
                            Task(
                                title = title,
                                description = description.takeIf { it.isNotBlank() },
                                priority = priority,
                                dueDate = dueDateMillis
                            )
                        )
                        navController.popBackStack()
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(R.string.save_task))
            }
        }
    }
}
