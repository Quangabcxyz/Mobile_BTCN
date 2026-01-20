package com.example.api

import retrofit2.http.GET

interface ApiService {
    @GET("v2/product")
    suspend fun getProduct(): ProductModel
}