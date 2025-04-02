package com.example.taskify.ui.screens.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.taskify.data.model.Priority
import com.example.taskify.data.model.Task
import com.example.taskify.ui.components.FilterDropdown
import com.example.taskify.ui.components.SwipeToDeleteTaskCard
import com.example.taskify.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: TaskViewModel,
    navController: NavController
) {
    val allTasks by viewModel.tasks.collectAsState() // observe task list from ViewModel

    var selectedTab by remember { mutableStateOf(0) } // 0 = All Tasks, 1 = Completed only
    val tabTitles = listOf("All Tasks", "Completed")

    var selectedPriority by remember { mutableStateOf("All") } // priority filter dropdown
    val priorityOptions = listOf("All", "Low", "Medium", "High")

    var showHelpDialog by remember { mutableStateOf(false) } // help button to explore features of the app

    // filtering tasks based on selected tab and priority
    val filteredTasks = remember(allTasks, selectedTab, selectedPriority) {
        allTasks.filter { task ->
            val matchesTab = when (selectedTab) {
                0 -> !task.isDone  // tskes in All tab
                1 -> task.isDone   // taskes in Completed tab
                else -> true
            }

            val matchesPriority = when (selectedPriority) {
                "Low" -> task.priority == Priority.LOW
                "Medium" -> task.priority == Priority.MEDIUM
                "High" -> task.priority == Priority.HIGH
                else -> true
            }

            matchesTab && matchesPriority
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Taskify (Your Task Manager)") },
                    actions = {
                        IconButton(onClick = { showHelpDialog = true }) {
                            Icon(Icons.Default.Info, contentDescription = "Help") // info icon to open help
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTab) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index }, // switch between All and Completed tabs
                            text = { Text(title) }
                        )
                    }
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addEdit?taskId=-1") // navigate to Add Task screen
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },

        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // dropdown to filter tasks by priority
                FilterDropdown(
                    selected = selectedPriority,
                    options = priorityOptions,
                    onSelected = { selectedPriority = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // show empty message or list of filtered tasks
                if (filteredTasks.isEmpty()) {
                    Text("No tasks to show here 😴")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(filteredTasks, key = { it.id }) { task ->
                            SwipeToDeleteTaskCard(
                                task = task,
                                showCompletedDate = (selectedTab == 1), // only show completed date in completed tab
                                onClick = {
                                    navController.navigate("addEdit?taskId=${task.id}") // edit task
                                },
                                onLongClick = { task ->
                                    // mark/unmark task as done
                                    val updatedTask = if (!task.isDone) {
                                        task.copy(isDone = true, completedAt = System.currentTimeMillis())
                                    } else {
                                        task.copy(isDone = false, completedAt = null)
                                    }
                                    viewModel.updateTask(updatedTask)
                                },
                                onDeleteConfirmed = { taskToDelete ->
                                    viewModel.deleteTask(taskToDelete) // delete task from db
                                }
                            )
                        }
                    }
                }
            }
        }
    )

    // help dialog that explains the app features to users
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = { Text("Need Help? 🧠") },
            text = {
                Text(
                    "• Tap a task to edit ✍️\n" +
                            "• Hold a task to mark as done ✅\n" +
                            "• Swipe left to delete 🗑️\n" +
                            "• Use the tabs to switch between All & Completed 🗂️"
                )
            },
            confirmButton = {
                TextButton(onClick = { showHelpDialog = false }) {
                    Text("Got it!")
                }
            }
        )
    }
}
