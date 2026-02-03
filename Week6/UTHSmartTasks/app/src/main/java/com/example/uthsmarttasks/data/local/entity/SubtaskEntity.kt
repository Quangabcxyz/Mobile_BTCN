package com.example.uthsmarttasks.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "subtasks",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class SubtaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val title: String,
    val isCompleted: Boolean = false
)
