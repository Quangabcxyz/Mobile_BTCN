package com.example.manhinhvd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PreviewBaiTap()
                }
            }
        }
    }
}
data class SinhVien(
    val ten: String,      // không thể rỗng
    val tieuSu: String?  // có thể có hoặc rỗng
)

@Composable
fun StudentProfile(sv: SinhVien) {
    Card(
        modifier = Modifier.padding(16.dp).fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ten sv không bao gio null nen hien thi luon
            Text(
                text = "Tên: ${sv.ten}",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tiểu sử: ${sv.tieuSu ?: "Chưa cập nhật thông tin"}", // (Elvis) chống màn hình trắng
                style = MaterialTheme.typography.bodyMedium,
                color = if (sv.tieuSu == null) Color.Gray else Color.Black
            )
            sv.tieuSu?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "(Đã xác thực nội dung: $it)", color = Color.Blue)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBaiTap() {
    Column {
        StudentProfile(SinhVien("Nguyễn Hồng Quang", "thích xem phim"))

        StudentProfile(SinhVien("Nguyễn Hồng B", null))
    }
}
