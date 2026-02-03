package com.example.uthsmarttasks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.uthsmarttasks.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<TaskEntity>>
    
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE remoteId = :remoteId LIMIT 1")
    suspend fun getTaskByRemoteId(remoteId: String): TaskEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long
    
    @Update
    suspend fun updateTask(task: TaskEntity)
    
    @Delete
    suspend fun deleteTask(task: TaskEntity)
    
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getTaskCount(): Int
}
