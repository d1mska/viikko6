package com.example.viikko6.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.viikko6.data.local.AppDatabase
import com.example.viikko6.data.model.TaskEntity
import com.example.viikko6.data.repository.TaskRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(application: Application)
    : AndroidViewModel(application) {

    private val repository: TaskRepository

    val tasks: StateFlow<List<TaskEntity>>

    private val _selectedTask = MutableStateFlow<TaskEntity?>(null)
    val selectedTask: StateFlow<TaskEntity?> = _selectedTask
    var showOnlyDone by mutableStateOf(false)
    var showAddDialog by mutableStateOf(false)
        private set

    init {
        val dao = AppDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(dao)

        tasks = repository.allTasks
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
    }
    fun filterByDone(tasks: List<TaskEntity>): List<TaskEntity> { return tasks.filter{!it.done} }
    fun onTaskSelected(task: TaskEntity) {
        _selectedTask.value = task
    }
    private val _newTask = MutableStateFlow("")
    val newTask: StateFlow<String> = _newTask.asStateFlow()
    fun closeDialog() {
        _selectedTask.value = null
    }

    fun openAddDialog() {
        showAddDialog = true
    }

    fun closeAddDialog() {
        showAddDialog = false
    }

    fun addTask(title: String, description: String, dueDate: LocalDate) {
        viewModelScope.launch {
            repository.insert(
                TaskEntity(
                    title = title,
                    description = description,
                    priority = 1,
                    dueDate = dueDate,
                    done = false
                )
            )
        }
    }
    //fun sortByDueDate(){ _tasks.value = _tasks.value.sortedBy { it.dueDate } }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.update(task)
            _selectedTask.value = null
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.delete(task)
            _selectedTask.value = null
        }
    }

    fun toggleDone(task: TaskEntity) {
        viewModelScope.launch {
            repository.update(task.copy(done = !task.done))
        }
    }
}