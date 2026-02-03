package com.example.uthsmarttasks.utils

fun dichDoUuTien(priority: String): String {
    return when (priority) {
        "High" -> "Cao"
        "Medium" -> "Trung bình"
        "Low" -> "Thấp"
        else -> priority
    }
}

fun dichTrangThai(status: String): String {
    return when (status) {
        "In Progress" -> "Đang làm"
        "Completed" -> "Hoàn thành"
        "Pending" -> "Chờ xử lý"
        else -> status
    }
}
