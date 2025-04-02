package com.example.taskify.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskify.data.model.Task
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteTaskCard(
    task: Task,
    onClick: () -> Unit = {},
    onLongClick: (Task) -> Unit = {},
    onDeleteConfirmed: (Task) -> Unit,
    showCompletedDate: Boolean = false
) {
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                showDialog = true
                false // 👈 Don't allow full swipe; we'll show dialog manually
            } else {
                true
            }
        }
    )

    // reset everything if task changes (for ex : after editing)
    LaunchedEffect(task.id) {
        dismissState.snapTo(SwipeToDismissBoxValue.Settled)
        showDialog = false
    }

    // swip to delete box with background
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        // red background while swiping
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Delete",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        content = {
            TaskCard(
                task = task,
                onClick = onClick,
                onLongClick = onLongClick,
                showCompletedDate = showCompletedDate
            )
        }
    )
    // confirm and cancel popup before deleting
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
            },
            title = { Text("Delete Task?") },
            text = { Text("Are you sure you want to delete this task?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteConfirmed(task)
                    showDialog = false
                    scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
                }) {
                    Text("Confirm", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    scope.launch { dismissState.snapTo(SwipeToDismissBoxValue.Settled) }
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}
