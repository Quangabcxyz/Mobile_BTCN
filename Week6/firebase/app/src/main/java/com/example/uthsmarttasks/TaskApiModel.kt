package com.example.firebase

// Khuôn đúc cho dữ liệu trả về từ API

// 1. Phản hồi chung (cho danh sách)
data class ApiResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: List<TaskApi>
)

// 2. Phản hồi chi tiết (cho từng task)
data class TaskDetailResponse(
    val isSuccess: Boolean,
    val message: String,
    val data: TaskApi
)

// 3. Đối tượng Task chính
data class TaskApi(
    val id: Int,
    val title: String,
    val description: String,
    val status: String,
    val priority: String,
    val dueDate: String,
    val desImageURL: String? = null,
    val category: String? = null,
    val subtasks: List<Subtask>? = emptyList(),     // Danh sách việc phụ
    val attachments: List<Attachment>? = emptyList() // Danh sách file đính kèm
)

// 4. Việc phụ (Subtask)
data class Subtask(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

// 5. File đính kèm (Attachment)
data class Attachment(
    val id: Int,
    val fileName: String,
    val fileUrl: String
)