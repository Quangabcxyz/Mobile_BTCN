@file:Suppress("DEPRECATION", "UNUSED_VARIABLE", "UNUSED_PARAMETER", "SPELL_CHECKING_INSPECTION", "UNUSED_VALUE", "UnusedAssignment")
package com.example.firebase

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
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

// DATA CLASS
data class Task(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val userId: String = "",
    val completed: Boolean = false
)

// ACTIVITY CHÍNH
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
    }
}

// 2. MÀN HÌNH LOGIN
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
            auth.signInWithCredential(credential)
                .addOnCompleteListener { taskLogin ->
                    if (taskLogin.isSuccessful) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        errorMessage = "Lỗi Firebase: ${taskLogin.exception?.message}"
                    }
                }
        } catch (e: ApiException) {
            errorMessage = "Lỗi Google: ${e.message}"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("UTH SmartTasks", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Spacer(modifier = Modifier.height(10.dp))
        Text("Ứng dụng quản lý công việc hiệu quả", color = Color.Gray)
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

// 3. MÀN HÌNH HOME
@Composable
fun ManHinhHome(navController: NavController) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {  }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid ?: ""

    var taskList by remember { mutableStateOf<List<Task>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    var newTaskDesc by remember { mutableStateOf("") }
    var currentTaskToEdit by remember { mutableStateOf<Task?>(null) }

    // Load dữ liệu
    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            db.collection("tasks")
                .whereEqualTo("userId", uid)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) return@addSnapshotListener
                    if (snapshots != null) {
                        taskList = snapshots.documents.map { doc ->
                            doc.toObject(Task::class.java)!!.copy(id = doc.id)
                        }
                    }
                }
        }
    }

    // Hàm Save
    fun saveTask() {
        if (newTaskTitle.isNotEmpty()) {
            if (currentTaskToEdit == null) {
                val newTask = Task(
                    title = newTaskTitle,
                    description = newTaskDesc,
                    userId = uid,
                    completed = false
                )
                db.collection("tasks").add(newTask)
            } else {
                val taskToUpdate = currentTaskToEdit!!
                db.collection("tasks").document(taskToUpdate.id)
                    .update(
                        mapOf(
                            "title" to newTaskTitle,
                            "description" to newTaskDesc
                        )
                    )
            }
            newTaskTitle = ""
            newTaskDesc = ""
            currentTaskToEdit = null
            showDialog = false
        }
    }

    fun deleteTask(taskId: String) {
        db.collection("tasks").document(taskId).delete()
    }

    fun toggleTaskStatus(task: Task) {
        db.collection("tasks").document(task.id)
            .update("completed", !task.completed)
    }

    fun openEditDialog(task: Task) {
        currentTaskToEdit = task
        newTaskTitle = task.title
        newTaskDesc = task.description
        showDialog = true
    }

    fun openAddDialog() {
        currentTaskToEdit = null
        newTaskTitle = ""
        newTaskDesc = ""
        showDialog = true
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nhiệm vụ của tôi", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(Icons.Default.Person, contentDescription = "Hồ sơ", modifier = Modifier.size(32.dp))
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { openAddDialog() },
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp).background(Color(0xFFF5F5F5)),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(taskList) { task ->
                TaskItem(
                    task = task,
                    onDelete = { deleteTask(task.id) },
                    onToggle = { toggleTaskStatus(task) },
                    onEdit = { openEditDialog(task) }
                )
            }
        }

        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = if (currentTaskToEdit == null) "Nhiệm vụ mới" else "Chỉnh sửa",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = newTaskTitle,
                            onValueChange = { newTaskTitle = it },
                            label = { Text("Tiêu đề") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = newTaskDesc,
                            onValueChange = { newTaskDesc = it },
                            label = { Text("Mô tả") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            TextButton(onClick = { showDialog = false }) { Text("Hủy") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { saveTask() },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                            ) {
                                Text(if (currentTaskToEdit == null) "Thêm" else "Cập nhật")
                            }
                        }
                    }
                }
            }
        }
    }
}

// ITEM GIAO DIỆN
@Composable
fun TaskItem(task: Task, onDelete: () -> Unit, onToggle: () -> Unit, onEdit: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onToggle() }
            )
            Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text(
                    text = task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (task.completed) Color.Gray else Color.Black,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else null
                )
                if (task.description.isNotEmpty()) {
                    Text(task.description, color = Color.Gray, fontSize = 14.sp)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
            }
        }
    }
}

// 4. MÀN HÌNH PROFILE
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
            db.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
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
        val userData = hashMapOf(
            "name" to (user?.displayName ?: ""),
            "email" to (user?.email ?: ""),
            "dob" to dateOfBirth,
            "avatarUrl" to avatarUrl
        )
        // Giữ nguyên "users"
        db.collection("users").document(uid).set(userData)
            .addOnSuccessListener {
                message = "Đã cập nhật thông tin!"
                isEditingInfo = false
            }
    }

    fun saveAvatar() {
        if (newAvatarLink.isNotEmpty()) {
            avatarUrl = newAvatarLink
            db.collection("users").document(uid).update("avatarUrl", newAvatarLink)
                .addOnSuccessListener {
                    message = "Đã đổi Avatar!"
                    showAvatarDialog = false
                    newAvatarLink = ""
                }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, bottom = 20.dp)
                .padding(horizontal = 16.dp)
        ) {
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text("< Quay lại", fontSize = 16.sp, color = Color.Gray)
            }
            Text(
                text = "Hồ sơ",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Avatar
        Box(contentAlignment = Alignment.BottomEnd) {
            AsyncImage(
                model = avatarUrl.ifEmpty { user?.photoUrl },
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .border(2.dp, Color.White, CircleShape),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = { showAvatarDialog = true },
                modifier = Modifier
                    .size(36.dp)
                    .offset(x = 4.dp, y = 4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3))
                    .border(2.dp, Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Thay đổi hình đại diện",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Info Card
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tên", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(user?.displayName ?: "Không tên", fontSize = 16.sp)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))

                Text("Email", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(user?.email ?: "Không Email", fontSize = 16.sp)
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFEEEEEE))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ngày sinh", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)

                        if (isEditingInfo) {
                            OutlinedTextField(
                                value = dateOfBirth,
                                onValueChange = { dateOfBirth = it },
                                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                singleLine = true
                            )
                        } else {
                            Text(dateOfBirth, fontSize = 16.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                    IconButton(onClick = {
                        if (isEditingInfo) saveInfo() else isEditingInfo = true
                    }) {
                        Icon(
                            imageVector = if (isEditingInfo) Icons.Default.Check else Icons.Default.Edit,
                            contentDescription = "Chỉnh sửa thông tin",
                            tint = if (isEditingInfo) Color(0xFF4CAF50) else Color.Gray
                        )
                    }
                }
            }
        }
        if (message.isNotEmpty()) {
            Text(message, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                auth.signOut()
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
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
                    OutlinedTextField(
                        value = newAvatarLink,
                        onValueChange = { newAvatarLink = it },
                        placeholder = { Text("https://...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = { saveAvatar() }) {
                    Text("Lưu Ảnh")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAvatarDialog = false }) {
                    Text("Hủy")
                }
            },
            containerColor = Color.White
        )
    }
}