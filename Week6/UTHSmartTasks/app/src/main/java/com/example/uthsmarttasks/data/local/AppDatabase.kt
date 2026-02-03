package com.example.uthsmarttasks.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.uthsmarttasks.data.local.dao.SubtaskDao
import com.example.uthsmarttasks.data.local.dao.TaskDao
import com.example.uthsmarttasks.data.local.entity.SubtaskEntity
import com.example.uthsmarttasks.data.local.entity.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [TaskEntity::class, SubtaskEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubtaskDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "uthsmarttasks_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Seed initial data
                        INSTANCE?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                seedDatabase(database.taskDao(), database.subtaskDao())
                            }
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        // Seed với 3 task mẫu
        private suspend fun seedDatabase(taskDao: TaskDao, subtaskDao: SubtaskDao) {
            val task1Id = taskDao.insertTask(
                TaskEntity(
                    title = "Hoàn thành bài tập Android",
                    description = "Làm bài tập về Jetpack Compose và Navigation",
                    status = "In Progress",
                    priority = "High",
                    dueDate = "2026-02-10",
                    category = "Học tập"
                )
            )
            subtaskDao.insertSubtask(SubtaskEntity(taskId = task1Id.toInt(), title = "Đọc tài liệu", isCompleted = true))
            subtaskDao.insertSubtask(SubtaskEntity(taskId = task1Id.toInt(), title = "Viết code", isCompleted = false))
            subtaskDao.insertSubtask(SubtaskEntity(taskId = task1Id.toInt(), title = "Test ứng dụng", isCompleted = false))

            taskDao.insertTask(
                TaskEntity(
                    title = "Đi chợ mua sắm",
                    description = "Mua rau củ quả và thực phẩm cho tuần này",
                    status = "Pending",
                    priority = "Medium",
                    dueDate = "2026-02-05",
                    category = "Cá nhân"
                )
            )

            taskDao.insertTask(
                TaskEntity(
                    title = "Họp nhóm dự án",
                    description = "Thảo luận về tiến độ và phân công công việc",
                    status = "Completed",
                    priority = "High",
                    dueDate = "2026-02-03",
                    category = "Công việc"
                )
            )
        }
    }
}
