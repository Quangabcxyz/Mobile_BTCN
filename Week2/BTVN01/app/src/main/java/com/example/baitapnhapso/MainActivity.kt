package com.example.baitapnhapso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // giao diện chính
            MainScreen()
        } 
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    var textInput by remember { mutableStateOf("") }

    var dataList by remember { mutableStateOf(listOf<Int>()) }

    var isError by remember { mutableStateOf(false) }

    // GIAO DIỆN
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Thực hành 02",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 25.dp)
        )

        // ô nhập va nút
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // nhập
            OutlinedTextField(
                value = textInput,
                onValueChange = {
                    textInput = it
                    isError = false
                },
                label = { Text("Nhập vào số lượng") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp),
                shape = RoundedCornerShape(10.dp),
                // chỉ cho phép bàn phím hiện số
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            // nút tạo
            Button(
                onClick = {
                    val number = textInput.toIntOrNull()
                    // kiểm tra
                    if (number != null && number > 0) {

                        isError = false

                        dataList = (1..number).toList()
                    } else {
                        isError = true
                        dataList = emptyList()
                    }
                },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Tạo", fontSize = 16.sp)
            }
        }

        // thông báo lỗi
        if (isError) {
            Text(
                text = "Dữ liệu bạn nhập không hợp lệ",
                color = Color.Red,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp), // Khoảng cách giữa các phần tử
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dataList) { number ->
                Button(
                    onClick = {  },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(text = number.toString(), fontSize = 18.sp)
                }
            }
        }
    }
}