package com.example.uthsmarttasks.ui.screens

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uthsmarttasks.R
import com.example.uthsmarttasks.data.local.entity.TaskEntity
import com.example.uthsmarttasks.viewmodel.TaskViewModel
import com.example.uthsmarttasks.utils.dichDoUuTien
import com.example.uthsmarttasks.utils.dichTrangThai

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: TaskViewModel = viewModel()
) {
    val context = LocalContext.current
    val taskList by viewModel.allTasks.collectAsState()

    // Hàm toggle trạng thái
    fun toggleTaskStatus(task: TaskEntity) {
        val newStatus = if (task.status == "Completed") "In Progress" else "Completed"
        viewModel.updateTask(task.copy(status = newStatus))
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
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { /* Home */ }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color(0xFF1976D2))
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { navController.navigate("calendar") }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Calendar")
                    }
                }
                Box(modifier = Modifier.weight(1f)) {}
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { navController.navigate("docs") }) {
                        Icon(Icons.Default.Menu, contentDescription = "Docs")
                    }
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("add_task")
                },
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier.size(65.dp).offset(y = 45.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(30.dp))
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (taskList.isEmpty()) {
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
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).background(Color.White),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 130.dp)
                ) {
                    items(taskList) { task ->
                        TaskItemCard(
                            task = task,
                            onToggle = { toggleTaskStatus(task) },
                            onClick = { navController.navigate("detail/${task.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItemCard(task: TaskEntity, onToggle: () -> Unit, onClick: () -> Unit) {
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
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Black,
                    uncheckedColor = Color.Black,
                    checkmarkColor = Color.White
                ),
                modifier = Modifier.scale(1.2f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = task.description, maxLines = 2, fontSize = 12.sp, color = Color.DarkGray, lineHeight = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "Trạng thái: ${dichTrangThai(task.status)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text(text = task.dueDate, fontSize = 12.sp, color = Color.DarkGray)
                }
            }
        }
    }
}

