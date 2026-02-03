package com.example.uthsmarttasks.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.uthsmarttasks.data.local.entity.SubtaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtaskDao {
    @Query("SELECT * FROM subtasks WHERE taskId = :taskId")
    fun getSubtasksForTask(taskId: Int): Flow<List<SubtaskEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubtask(subtask: SubtaskEntity)
    
    @Update
    suspend fun updateSubtask(subtask: SubtaskEntity)
    
    @Query("DELETE FROM subtasks WHERE taskId = :taskId")
    suspend fun deleteSubtasksForTask(taskId: Int)
}
