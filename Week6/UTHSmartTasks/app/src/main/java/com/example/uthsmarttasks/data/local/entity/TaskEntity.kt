package com.example.uthsmarttasks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val status: String,
    val priority: String,
    val dueDate: String,
    val category: String,
    val userId: String = "",
    val remoteId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
