package com.example.taskify.ui.screens.addedit

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.taskify.data.model.Priority
import com.example.taskify.data.model.Task
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    onSaveClick: (Task) -> Unit,
    taskToEdit: Task? = null
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // tsak fields
    var title by remember(taskToEdit) { mutableStateOf(taskToEdit?.title ?: "") }
    var description by remember(taskToEdit) { mutableStateOf(taskToEdit?.description ?: "") }
    var priority by remember(taskToEdit) { mutableStateOf(taskToEdit?.priority ?: Priority.MEDIUM) }
    var dueDateTimestamp by remember(taskToEdit) { mutableStateOf(taskToEdit?.dueDate ?: 0L) }

    // calendar picker
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            calendar.set(year, month, day)
            dueDateTimestamp = calendar.timeInMillis
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskToEdit == null) "Add Task" else "Edit Task") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // check for input fields before save
                if (title.trim().isEmpty()) {
                    Toast.makeText(context, "Title can't be empty 📝", Toast.LENGTH_SHORT).show()
                    return@FloatingActionButton
                }

                if (dueDateTimestamp == 0L) {
                    Toast.makeText(context, "Due date is required 📅", Toast.LENGTH_SHORT).show()
                    return@FloatingActionButton
                }

                val today = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                if (dueDateTimestamp < today) {
                    Toast.makeText(context, "Due date can’t be in the past ⏰", Toast.LENGTH_SHORT).show()
                    return@FloatingActionButton
                }

                // prepare task object to save
                val task = Task(
                    id = taskToEdit?.id ?: 0,
                    title = title,
                    description = description,
                    priority = priority,
                    dueDate = if (dueDateTimestamp != 0L) dueDateTimestamp else null,
                    isDone = taskToEdit?.isDone ?: false
                )
                onSaveClick(task)
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // title input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // description input
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            //priority chips
            Row {
                Text("Priority: ")
                Priority.values().forEach {
                    AssistChip(
                        onClick = { priority = it },
                        label = { Text(it.name) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (priority == it) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // due date button
            Button(onClick = { datePickerDialog.show() }) {
                Text("Select Due Date")
            }

            // show selected date
            if (dueDateTimestamp != 0L) {
                Text("Selected: ${dateFormat.format(Date(dueDateTimestamp))}")
            }
        }
    }
}
