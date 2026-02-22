package com.example.viikko6.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "tasks")
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val priority: Int,
    val dueDate: LocalDate,
    val done: Boolean
)