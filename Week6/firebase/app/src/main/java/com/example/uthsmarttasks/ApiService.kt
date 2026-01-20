package com.example.firebase

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

// 1. Định nghĩa các lệnh gọi Server
interface ApiService {
    // Lấy danh sách Task
    @GET("tasks")
    suspend fun getTasks(): ApiResponse

    // Lấy chi tiết 1 Task (theo ID)
    @GET("task/{id}")
    suspend fun getTaskDetail(@Path("id") id: Int): TaskDetailResponse

    // Xóa 1 Task (theo ID)
    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): TaskDetailResponse
}

// 2. Cấu hình Retrofit (Cổng kết nối)
object RetrofitClient {
    private const val BASE_URL = "https://amock.io/api/researchUTH/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}