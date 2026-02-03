package com.example.uthsmarttasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthsmarttasks.data.local.AppDatabase
import com.example.uthsmarttasks.data.local.entity.SubtaskEntity
import com.example.uthsmarttasks.data.local.entity.TaskEntity
import com.example.uthsmarttasks.data.repository.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    
    val allTasks: StateFlow<List<TaskEntity>>
    
    init {
        val database = AppDatabase.getDatabase(application)
        repository = TaskRepository(database.taskDao(), database.subtaskDao())
        allTasks = repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        if (user != null) {
            viewModelScope.launch {
                repository.syncTasks(user.uid)
            }
        }
    }
    
    // Task operations
    fun addTask(task: TaskEntity) = viewModelScope.launch {
        val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser
        val taskWithUser = if (user != null) task.copy(userId = user.uid) else task
        repository.insertTask(taskWithUser)
    }
    
    fun updateTask(task: TaskEntity) = viewModelScope.launch {
        repository.updateTask(task)
    }
    
    fun deleteTask(task: TaskEntity) = viewModelScope.launch {
        repository.deleteTask(task)
    }
    
    suspend fun getTaskById(id: Int): TaskEntity? {
        return repository.getTaskById(id)
    }
    
    // Subtask operations
    fun getSubtasksForTask(taskId: Int) = repository.getSubtasksForTask(taskId)
    
    fun addSubtask(subtask: SubtaskEntity) = viewModelScope.launch {
        repository.insertSubtask(subtask)
    }
    
    fun updateSubtask(subtask: SubtaskEntity) = viewModelScope.launch {
        repository.updateSubtask(subtask)
    }
    
    fun deleteSubtasksForTask(taskId: Int) = viewModelScope.launch {
        repository.deleteSubtasksForTask(taskId)
    }
}
