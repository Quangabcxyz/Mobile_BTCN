package com.example.uthsmarttasks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    val uid = user?.uid ?: ""
    var dateOfBirth by remember { mutableStateOf("Đang tải...") }
    var avatarUrl by remember { mutableStateOf("") }
    var isEditingInfo by remember { mutableStateOf(false) }
    var showAvatarDialog by remember { mutableStateOf(false) }
    var newAvatarLink by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            db.collection("users").document(uid).get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    dateOfBirth = document.getString("dob") ?: "Chưa cập nhật"
                    avatarUrl = document.getString("avatarUrl") ?: ""
                } else {
                    dateOfBirth = "Chưa cập nhật"
                }
            }
        }
    }

    fun saveInfo() {
        val userData = hashMapOf("name" to (user?.displayName ?: ""), "email" to (user?.email ?: ""), "dob" to dateOfBirth, "avatarUrl" to avatarUrl)
        db.collection("users").document(uid).set(userData).addOnSuccessListener {
            message = "Đã cập nhật thông tin!"
            isEditingInfo = false
        }
    }

    fun saveAvatar() {
        if (newAvatarLink.isNotEmpty()) {
            avatarUrl = newAvatarLink
            db.collection("users").document(uid).update("avatarUrl", newAvatarLink).addOnSuccessListener {
                message = "Đã đổi Avatar!"
                showAvatarDialog = false
                newAvatarLink = ""
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp, bottom = 20.dp).padding(horizontal = 16.dp)) {
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.Black, modifier = Modifier.size(28.dp))
            }
            Text("Hồ sơ", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Center))
        }
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = avatarUrl.ifEmpty { user?.photoUrl },
                contentDescription = "Avatar",
                modifier = Modifier.size(120.dp).clip(CircleShape).background(Color.LightGray).border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { showAvatarDialog = true },
                modifier = Modifier.size(36.dp).offset(x = 4.dp, y = 4.dp).clip(CircleShape).background(Color(0xFF2196F3)).border(2.dp, Color.White, CircleShape)
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Đổi Avatar", tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tên", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(user?.displayName ?: "Không tên", fontSize = 16.sp)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                Text("Email", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(user?.email ?: "Không Email", fontSize = 16.sp)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ngày sinh", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        if (isEditingInfo) {
                            OutlinedTextField(value = dateOfBirth, onValueChange = { dateOfBirth = it }, modifier = Modifier.fillMaxWidth().padding(top = 4.dp), singleLine = true)
                        } else {
                            Text(dateOfBirth, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                    IconButton(onClick = { if (isEditingInfo) saveInfo() else isEditingInfo = true }) {
                        Icon(imageVector = if (isEditingInfo) Icons.Default.Check else Icons.Default.Edit, contentDescription = "Chỉnh sửa", tint = if (isEditingInfo) Color(0xFF4CAF50) else Color.Gray)
                    }
                }
            }
        }
        if (message.isNotEmpty()) Text(message, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                auth.signOut()
                navController.navigate("login") { popUpTo("home") { inclusive = true } }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE))
        ) { Text("Đăng xuất", color = Color.Red) }
        Spacer(modifier = Modifier.height(20.dp))
    }
    if (showAvatarDialog) {
        AlertDialog(
            onDismissRequest = { showAvatarDialog = false },
            title = { Text("Đổi Ảnh Đại Diện") },
            text = {
                Column {
                    Text("Dán đường link (URL) hình ảnh vào đây:", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = newAvatarLink, onValueChange = { newAvatarLink = it }, placeholder = { Text("https://...") }, modifier = Modifier.fillMaxWidth())
                }
            },
            confirmButton = { Button(onClick = { saveAvatar() }) { Text("Lưu Ảnh") } },
            dismissButton = { TextButton(onClick = { showAvatarDialog = false }) { Text("Hủy") } },
            containerColor = Color.White
        )
    }
}
