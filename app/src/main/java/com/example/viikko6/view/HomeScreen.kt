package com.example.viikko6.View

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.viikko6.viewmodel.TaskViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TaskViewModel) {
    val newTaskValue by viewModel.newTask.collectAsState()
    val tasks by viewModel.tasks.collectAsState()
    val selectedTask by viewModel.selectedTask.collectAsState()

    val visibleTasks = if (viewModel.showOnlyDone) {
        viewModel.filterByDone(tasks)
    } else {
        tasks
    }

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
                            navController.navigate("calendar")
                        }) {
                            Text(
                                text = "Calendar",
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)


            ) {
                Button(onClick = {
                    viewModel.showOnlyDone = !viewModel.showOnlyDone
                }) {
                    Text(
                        text = "Filter Done",
                        fontSize = 13.sp
                    )
                }
                Button(onClick = {
                    //viewModel.sortByDueDate()
                }) {
                    Text(text = "Sort Due Date")
                }

            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 15.dp
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visibleTasks) { task ->
                    Card(
                        modifier = Modifier
                            .background(color = Color.White)
                            .padding(8.dp)
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onTaskSelected(task)
                            }
                    ) {
                        Column() {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)),

                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Checkbox(
                                    checked = task.done,
                                    onCheckedChange = {
                                        viewModel.toggleDone(task)
                                    }
                                )
                                Text(
                                    "${task.title}",
                                    modifier = Modifier
                                        .weight(1f),
                                )
                                Text(
                                    "${task.dueDate}",
                                    modifier = Modifier
                                        .weight(1f),

                                    )
                            }
                            Text(
                                "${task.description}",
                                modifier = Modifier
                                    .padding(10.dp)
                            )
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
            deleteTask ={id -> viewModel.deleteTask(task = selectedTask!!)},
            onUpdate = {viewModel.updateTask(it)})
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