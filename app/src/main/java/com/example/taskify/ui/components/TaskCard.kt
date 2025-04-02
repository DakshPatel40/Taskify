package com.example.taskify.ui.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.taskify.data.model.Priority
import com.example.taskify.data.model.Task
import com.example.taskify.utils.formatDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit = {},
    onLongClick: (Task) -> Unit = {},
    modifier: Modifier = Modifier,
    showCompletedDate: Boolean = false
) {

    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick() },
                onLongClick = {
                    Log.d("TaskCard", "Long Pressed!")
                    onLongClick(task) // ✅ Pass the untouched task
                }
            ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    textDecoration = TextDecoration.None,
                    modifier = Modifier.weight(1f)
                )
                PriorityChip(priority = task.priority)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // description (only if not empty)
            if (!task.description.isNullOrBlank()) {
                Text(
                    text = task.description.trim(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))
            }

            task.dueDate?.let {
                Text(
                    text = "Due: ${formatDate(it)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Created: ${formatDate(task.createdAt)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (showCompletedDate && task.isDone) {
                Text(
                    text = "Completed: ${formatDate(task.completedAt ?: task.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}
