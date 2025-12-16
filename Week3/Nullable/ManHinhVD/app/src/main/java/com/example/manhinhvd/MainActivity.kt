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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(), // ne tai tho
                    color = MaterialTheme.colorScheme.background
                ) {
                    PreviewBaiTap()
                }
            }
        }
    }
}
data class SinhVien(val ten: String, val tieuSu: String?)

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

            // tieu su (null)
            // c1 : (?:) hien tra tri mac dịnh neu null
            Text(
                text = "Tiểu sử: ${sv.tieuSu ?: "Chưa cập nhật thông tin"}", // (Elvis)
                style = MaterialTheme.typography.bodyMedium,
                color = if (sv.tieuSu == null) Color.Gray else Color.Black
            )

            // c2  : .let de hien thi tphần UI khi co du lieu
            sv.tieuSu?.let { // (Safe Call + Let)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "(Đã xác thực nội dung: $it)", color = Color.Blue)
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PreviewBaiTap() {
    Column {
        // TH 1: Đầy đủ thông tin
        StudentProfile(SinhVien("Nguyễn Hồng Quang", "ngại va chạm"))

        // TH 2: Thiếu tiểu sử (NULL)
        StudentProfile(SinhVien("Nguyễn Hồng B", null))
    }
}