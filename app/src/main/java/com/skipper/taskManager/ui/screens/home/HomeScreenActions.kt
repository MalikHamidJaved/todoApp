package com.skipper.taskManager.ui.screens.home

import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skipper.taskManager.R
import com.skipper.taskManager.data.model.SortOption
import com.skipper.taskManager.data.model.TaskFilter

@Composable
fun HomeScreenActions(
    selectedFilter: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit,
    selectedSort: SortOption?,
    onSortSelected: (SortOption) -> Unit,
    onSettingsClicked: () -> Unit
) {
    var filterMenuExpanded by remember { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    IconButton(onClick = { filterMenuExpanded = true }) {
        Icon(
            painter = painterResource(R.drawable.ic_filter),
            contentDescription = "Filter",
            tint = Color.Gray,
            modifier = Modifier.size(25.dp)
        )
        DropdownMenu(
            expanded = filterMenuExpanded,
            onDismissRequest = { filterMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = {
                    onFilterSelected(TaskFilter.ALL)
                    filterMenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Completed") },
                onClick = {
                    onFilterSelected(TaskFilter.COMPLETED)
                    filterMenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Pending") },
                onClick = {
                    onFilterSelected(TaskFilter.PENDING)
                    filterMenuExpanded = false
                }
            )
        }
    }

    IconButton(onClick = { sortMenuExpanded = true }) {
        Icon(
            painter = painterResource(R.drawable.ic_sort),
            contentDescription = "Sort",
            tint = Color.Gray,
            modifier = Modifier.size(25.dp)
        )
        DropdownMenu(
            expanded = sortMenuExpanded,
            onDismissRequest = { sortMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("By Priority") },
                onClick = {
                    onSortSelected(SortOption.PRIORITY)
                    sortMenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("By Due Date") },
                onClick = {
                    onSortSelected(SortOption.DUE_DATE)
                    sortMenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Alphabetically") },
                onClick = {
                    onSortSelected(SortOption.ALPHABETICAL)
                    sortMenuExpanded = false
                }
            )
        }
    }


    IconButton(onClick = {  onSettingsClicked.invoke() }) {
        Icon(
            painter = painterResource(R.drawable.ic_settings),
            contentDescription = "Settings",
            tint = Color.Gray,
            modifier = Modifier.size(25.dp)
        )

    }
}
