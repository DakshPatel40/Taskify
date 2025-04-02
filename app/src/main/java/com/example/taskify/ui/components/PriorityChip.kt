package com.example.taskify.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.taskify.data.model.Priority

@Composable
fun PriorityChip(priority: Priority) {
    val color = when (priority) {
        Priority.HIGH -> Color(0xFFD32F2F)   // Red
        Priority.MEDIUM -> Color(0xFFFFA000) // Orange
        Priority.LOW -> Color(0xFF388E3C)    // Green
    }

    AssistChip(
        onClick = { }, // readonly display chip
        label = { Text(priority.name) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = 0.15f),
            labelColor = color // set label text color here
        )
    )
}