package com.example.viikko6.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viikko6.data.model.TaskEntity
import java.time.LocalDate

@Composable
fun DetailDialog(task: TaskEntity,
                 onClose: () -> Unit,
                 onUpdate: (TaskEntity) -> Unit,
                 deleteTask: (Int) -> Unit
){
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var dueDateText by remember { mutableStateOf(task.dueDate.toString()) }


    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(text = "Edit task")
        },
        text = {
            Column{
                TextField(value = title, onValueChange = {title = it})
                TextField(value = description, onValueChange = {description = it})
                TextField(value = dueDateText, onValueChange = {dueDateText = it})

            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    deleteTask(task.id)
                    onClose()
                }) {
                    Text(text = "Delete")
                }
                Button(onClick = onClose) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = {
                        val parsedDate = runCatching {
                            LocalDate.parse(dueDateText)
                        }.getOrElse {
                            task.dueDate
                        }

                        onUpdate(task.copy(title = title, description = description, dueDate = parsedDate))
                    }
                ) {
                    Text("save")
                }
            }
        }
    )
}


@Composable
fun AddTaskDialog(
    onClose: () -> Unit,
    onSave: (String, String, LocalDate) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDateText by remember {
        mutableStateOf(LocalDate.now().toString())
    }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text("Add task") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                TextField(
                    value = dueDateText,
                    onValueChange = { dueDateText = it },
                    label = { Text("Due date (yyyy-MM-dd)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val date = runCatching {
                    LocalDate.parse(dueDateText)
                }.getOrElse { LocalDate.now() }

                onSave(title, description, date)
                onClose()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onClose) {
                Text("Cancel")
            }
        }
    )
}