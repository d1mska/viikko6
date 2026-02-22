package com.example.viikko6.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.viikko6.viewmodel.TaskViewModel
import com.example.viikko6.data.model.TaskEntity


@Composable
fun Calendar(
    navController: NavController,
    viewModel: TaskViewModel
) {
    val tasks by viewModel.tasks.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()

    val tasksByDate = tasks
        .sortedBy { it.dueDate }
        .groupBy { it.dueDate }
    Scaffold(
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
            ) {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            navController.navigate("home")
                        }) {
                            Text(
                                text = "Home",
                                fontSize = 13.sp
                            )
                        }
                        Button(onClick = { viewModel.openAddDialog() }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            tasksByDate.forEach { (date, dayTasks) ->

                item {
                    Text(
                        text = date.toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(dayTasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onTaskSelected(task)
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = task.done,
                                onCheckedChange = {
                                    viewModel.toggleDone(task)
                                }
                            )
                            Text(task.title)
                        }
                    }
                }
            }
        }
    }

    if (selectedTask != null) {
        DetailDialog(
            task = selectedTask!!,
            onClose = {viewModel.closeDialog()},
            deleteTask = { viewModel.deleteTask(selectedTask!!) },
            onUpdate = { task -> viewModel.updateTask(task)})
    }

    if (viewModel.showAddDialog) {
        AddTaskDialog(
            onClose = { viewModel.closeAddDialog() },
            onSave = { title, description, dueDate ->
                viewModel.addTask(title, description, dueDate)
            }
        )
    }
}