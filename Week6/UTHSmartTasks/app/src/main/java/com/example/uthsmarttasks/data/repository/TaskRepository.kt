package com.example.uthsmarttasks.data.repository

import com.example.uthsmarttasks.data.local.dao.SubtaskDao
import com.example.uthsmarttasks.data.local.dao.TaskDao
import com.example.uthsmarttasks.data.local.entity.SubtaskEntity
import com.example.uthsmarttasks.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class TaskRepository(
    private val taskDao: TaskDao,
    private val subtaskDao: SubtaskDao
) {
    // Tasks
    val allTasks: Flow<List<TaskEntity>> = taskDao.getAllTasks()
    private val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()

    suspend fun syncTasks(userId: String) {
        try {
            val snapshot = firestore.collection("users").document(userId).collection("tasks").get().await()
            val remoteTasks = snapshot.toObjects(TaskEntity::class.java)
            remoteTasks.forEach { remoteTask ->
                 val existing = taskDao.getTaskByRemoteId(remoteTask.remoteId)
                 if (existing != null) {
                     taskDao.updateTask(remoteTask.copy(id = existing.id))
                 } else {
                     taskDao.insertTask(remoteTask)
                 }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    
    suspend fun getTaskById(id: Int): TaskEntity? = taskDao.getTaskById(id)
    
    suspend fun insertTask(task: TaskEntity): Long {
        val remoteId = java.util.UUID.randomUUID().toString()
        val taskWithRemote = task.copy(remoteId = remoteId)
        val id = taskDao.insertTask(taskWithRemote)
        
        if (task.userId.isNotBlank()) {
            saveToFirestore(taskWithRemote.copy(id = id.toInt()))
        }
        return id
    }
    
    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
        if (task.userId.isNotBlank()) {
            saveToFirestore(task)
        }
    }
    
    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
        if (task.userId.isNotBlank() && task.remoteId.isNotBlank()) {
            firestore.collection("users").document(task.userId).collection("tasks").document(task.remoteId).delete()
        }
    }

    private fun saveToFirestore(task: TaskEntity) {
        try {
            firestore.collection("users").document(task.userId).collection("tasks")
                .document(task.remoteId)
                .set(task)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Subtasks
    fun getSubtasksForTask(taskId: Int): Flow<List<SubtaskEntity>> =
        subtaskDao.getSubtasksForTask(taskId)
    
    suspend fun insertSubtask(subtask: SubtaskEntity) = 
        subtaskDao.insertSubtask(subtask)
    
    suspend fun updateSubtask(subtask: SubtaskEntity) = 
        subtaskDao.updateSubtask(subtask)
    
    suspend fun deleteSubtasksForTask(taskId: Int) = 
        subtaskDao.deleteSubtasksForTask(taskId)
}
