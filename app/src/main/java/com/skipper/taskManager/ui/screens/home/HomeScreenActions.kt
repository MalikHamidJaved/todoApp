package com.skipper.taskManager.ui.screens.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
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
import com.skipper.taskManager.data.model.SortOption
import com.skipper.taskManager.data.model.TaskFilter

@Composable
fun HomeScreenActions(
    selectedFilter: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit,
    selectedSort: SortOption?,
    onSortSelected: (SortOption) -> Unit
) {
    var filterMenuExpanded by remember { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    IconButton(onClick = { filterMenuExpanded = true }) {
        Icon(Icons.Default.Refresh, contentDescription = "Filter")
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
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Sort")
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
}
