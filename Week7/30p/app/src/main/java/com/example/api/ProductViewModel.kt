package com.example.api

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log

class ProductViewModel : ViewModel() {
    var product by mutableStateOf<ProductModel?>(null)

    init {
        getProductData()
    }

    private fun getProductData() {
        viewModelScope.launch {
            try {
                Log.d("LoiAPI", "Bắt đầu gọi API...")

                val apiService = RetrofitHelper.getInstance().create(ApiService::class.java)
                product = apiService.getProduct()

                Log.d("LoiAPI", "Gọi API thành công: ${product?.name}")

            } catch (e: Exception) {
                Log.e("LoiAPI", "Gọi API thất bại rồi: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}