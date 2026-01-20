@file:Suppress("DEPRECATION", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "SPELL_CHECKING_INSPECTION", "UNUSED_VALUE", "UnusedAssignment")
package com.example.uthsmarttasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings


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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

// 1. QUẢN LÝ ĐIỀU HƯỚNG
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { ManHinhLogin(navController) }
        composable("home") { ManHinhHome(navController) }
        composable("profile") { ManHinhProfile(navController) }
        composable("detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            ManHinhDetail(navController, taskId)
        }
    }
}

// 2. MÀN HÌNH HOME
@Composable
fun ManHinhHome(navController: NavController) {
    val context = LocalContext.current // Để dùng Toast
    var taskList by remember { mutableStateOf<List<TaskApi>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf("") }

    // Gọi API lấy danh sách
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getTasks()
            if (response.isSuccess) {
                taskList = response.data
            } else {
                errorMessage = "Lỗi: ${response.message}"
            }
        } catch (e: Exception) {
            errorMessage = "Lỗi mạng: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // Hàm giả lập check/uncheck
    fun toggleTaskStatus(taskId: Int) {
        taskList = taskList.map { task ->
            if (task.id == taskId) {
                val newStatus = if (task.status == "Completed") "In Progress" else "Completed"
                task.copy(status = newStatus)
            } else {
                task
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.uth),
                        contentDescription = "Logo",
                        modifier = Modifier.size(50.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("UTH SmartTasks", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                        Text("Quản lý công việc hiệu quả", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(Icons.Default.Person, contentDescription = "Profile", modifier = Modifier.size(32.dp))
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                contentColor = Color.Gray,
                tonalElevation = 8.dp,
                contentPadding = PaddingValues(0.dp)
            ) {
                // 1. Nút Home
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { /* Home */ }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF1976D2))
                    }
                }

                // 2. Nút Lịch
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { Toast.makeText(context, "Lịch", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Calendar")
                    }
                }

                // 3. nút +
                Box(modifier = Modifier.weight(1f)) {}

                // 4. Nút tài liệu
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { Toast.makeText(context, "Tài liệu", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Menu, contentDescription = "Docs")
                    }
                }

                // 5. Nút cài đặt
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { Toast.makeText(context, "Cài đặt", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, "API thêm mới chưa được cung cấp!", Toast.LENGTH_SHORT).show()
                },
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier
                    .size(65.dp)
                    .offset(y = 45.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(30.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, modifier = Modifier.align(Alignment.Center))
            } else if (taskList.isEmpty()) {
                // Empty View
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Empty",
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Danh sách trống!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    Text("Hãy thêm công việc mới để bắt đầu", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .background(Color.White),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 130.dp)
                ) {
                    items(taskList) { task ->
                        TaskApiItem(
                            task = task,
                            onToggle = { toggleTaskStatus(task.id) },
                            onClick = { navController.navigate("detail/${task.id}") }
                        )
                    }
                }
            }
        }
    }
}

// ITEM GIAO DIỆN
@Composable
fun TaskApiItem(task: TaskApi, onToggle: () -> Unit, onClick: () -> Unit) {
    val backgroundColor = when (task.priority) {
        "High" -> Color(0xFFFFCDD2)
        "Medium" -> Color(0xFFFFF9C4)
        else -> Color(0xFFF5F5F5)
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.status == "Completed",
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Color.Black, uncheckedColor = Color.Black, checkmarkColor = Color.White),
                modifier = Modifier.scale(1.2f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = task.description, maxLines = 2, fontSize = 12.sp, color = Color.DarkGray, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    // Dịch trạng thái sang tiếng Việt
                    Text(text = "Trạng thái: ${dichTrangThai(task.status)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = task.dueDate.take(10), fontSize = 12.sp, color = Color.DarkGray)
                }
            }
        }
    }
}

// 4. MÀN HÌNH CHI TIẾT
@Composable
fun ManHinhDetail(navController: NavController, taskId: Int) {
    var task by remember { mutableStateOf<TaskApi?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Load dữ liệu
    LaunchedEffect(taskId) {
        try {
            val response = RetrofitClient.api.getTaskDetail(1) // Ép ID 1
            if (response.isSuccess) {
                task = response.data
            } else {
                message = "Lỗi: ${response.message}"
            }
        } catch (e: Exception) {
            message = "Lỗi kết nối: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    // XỬ LÝ TÍCH VÀO SUBTASK
    fun toggleSubtask(subId: Int) {
        // Kiểm tra task không null
        task?.let { currentTask ->
            val updatedSubtasks = currentTask.subtasks?.map { sub ->
                if (sub.id == subId) {
                    sub.copy(isCompleted = !sub.isCompleted)
                } else {
                    sub
                }
            }
            task = currentTask.copy(subtasks = updatedSubtasks)
        }
    }

    fun deleteTask() {
        isLoading = true
        scope.launch {
            try {
                val response = RetrofitClient.api.deleteTask(1)
                if (response.isSuccess) {
                    navController.popBackStack()
                } else {
                    message = "Xóa thất bại: ${response.message}"
                    isLoading = false
                }
            } catch (e: Exception) {
                message = "Lỗi khi xóa: ${e.message}"
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = Color.Black, modifier = Modifier.size(28.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("Chi tiết", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { deleteTask() }) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (task != null) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {

                    // Tiêu đề va Tag
                    Text(task!!.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Badge(containerColor = Color(0xFFE3F2FD)) { Text(task!!.category ?: "Chung", modifier = Modifier.padding(4.dp)) }
                        Badge(containerColor = if (task!!.priority == "High") Color(0xFFFFEBEE) else Color(0xFFE8F5E9)) {
                            Text(dichDoUuTien(task!!.priority), color = if (task!!.priority == "High") Color.Red else Color.Green, modifier = Modifier.padding(4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Mô tả:", fontWeight = FontWeight.Bold)
                    Text(task!!.description, color = Color.Gray)

                    // PHẦN SUBTASK
                    if (!task!!.subtasks.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Công việc phụ:", fontWeight = FontWeight.Bold)

                        task!!.subtasks!!.forEach { sub ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { toggleSubtask(sub.id) }
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = sub.isCompleted,
                                    onCheckedChange = { toggleSubtask(sub.id) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF1976D2),
                                        checkmarkColor = Color.White
                                    )
                                )
                                Text(
                                    text = sub.title,
                                    textDecoration = if (sub.isCompleted) TextDecoration.LineThrough else null,
                                    color = if (sub.isCompleted) Color.Gray else Color.Black
                                )
                            }
                        }
                    }

                    if (!task!!.attachments.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Tệp đính kèm:", fontWeight = FontWeight.Bold)
                        task!!.attachments!!.forEach { file ->
                            Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(file.fileName, color = Color.Blue, textDecoration = TextDecoration.Underline)
                            }
                        }
                    }
                }
            } else if (message.isNotEmpty()) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(message, color = Color.Red, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

// 5. MÀN HÌNH LOGIN
@Suppress("DEPRECATION")
@Composable
fun ManHinhLogin(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var errorMessage by remember { mutableStateOf("") }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("236812771722-djrnnpen5ok9t9orm3okfmopfohmbsj5.apps.googleusercontent.com")
        .requestEmail()
        .build()
    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener { taskLogin ->
                if (taskLogin.isSuccessful) {
                    navController.navigate("home") { popUpTo("login") { inclusive = true } }
                } else {
                    errorMessage = "Lỗi: ${taskLogin.exception?.message}"
                }
            }
        } catch (e: ApiException) { errorMessage = "Lỗi Google: ${e.message}" }
    }

    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(
            painter = painterResource(id = R.drawable.uth),
            contentDescription = "Logo UTH",
            modifier = Modifier.height(120.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text("UTH SmartTasks", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Spacer(modifier = Modifier.height(8.dp))
        Text("Ứng dụng quản lý công việc đơn giản và hiệu quả.", color = Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = { launcher.launch(googleSignInClient.signInIntent) },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Đăng nhập bằng Google")
        }
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(errorMessage, color = Color.Red, fontSize = 12.sp)
        }
    }
}

// 6. MÀN HÌNH PROFILE
@Composable
fun ManHinhProfile(navController: NavController) {
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