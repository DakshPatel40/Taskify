@file:Suppress("UNCHECKED_CAST")
package com.example.taskify

import android.os.Bundle
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.taskify.data.db.AppDatabase
import com.example.taskify.data.db.MIGRATION_1_2
import com.example.taskify.data.model.Task
import com.example.taskify.data.repository.TaskRepository
import com.example.taskify.ui.screens.addedit.AddEditTaskScreen
import com.example.taskify.ui.screens.home.HomeScreen
import com.example.taskify.ui.theme.TaskifyTheme
import com.example.taskify.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // setup Room DB
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "taskify-db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()

        // setup repository + ViewModel
        val repo = TaskRepository(db.taskDao())
        val viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(repo) as T
                }
            }
        )[TaskViewModel::class.java]

        setContent {
            TaskifyTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                // check if it's the user's first time
                val prefs = context.getSharedPreferences("taskify_prefs", Context.MODE_PRIVATE)
                val isFirstLaunch = prefs.getBoolean("first_time_user", true)
                var showTutorial by remember { mutableStateOf(isFirstLaunch) }

                // mark as not first time after first launch
                LaunchedEffect(showTutorial) {
                    if (showTutorial) {
                        prefs.edit().putBoolean("first_time_user", false).apply()
                    }
                }

                // tutorial popup for first-time users
                if (showTutorial) {
                    AlertDialog(
                        onDismissRequest = { showTutorial = false },
                        title = { Text("Welcome to Taskify 🚀") },
                        text = {
                            Text(
                                "Here's how to use the app:\n\n" +
                                        "• Tap a task to edit ✍️\n" +
                                        "• Hold a task to mark as done ✅\n" +
                                        "• Swipe left to delete 🗑️\n" +
                                        "• Use tabs to filter tasks by status\n" +
                                        "\nHappy productivity! 💪"
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { showTutorial = false }) {
                                Text("Got it!")
                            }
                        }
                    )
                }

                // Navigation
                NavHost(navController = navController, startDestination = "home") {
                    // home screen route
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }

                    // add/edit screen with optional taskId
                    composable(
                        "addEdit?taskId={taskId}",
                        arguments = listOf(navArgument("taskId") {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
                        var task by remember { mutableStateOf<Task?>(null) }

                        // load task if editing
                        LaunchedEffect(taskId) {
                            if (taskId != -1) {
                                task = viewModel.getTaskById(taskId)
                            }
                        }

                        AddEditTaskScreen(
                            taskToEdit = task,
                            onSaveClick = { taskToSave ->
                                if (taskToSave.id != 0) viewModel.updateTask(taskToSave)
                                else viewModel.insertTask(taskToSave)
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
