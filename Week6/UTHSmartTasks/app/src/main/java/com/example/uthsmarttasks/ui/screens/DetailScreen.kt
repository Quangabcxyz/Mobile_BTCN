package com.example.uthsmarttasks.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.uthsmarttasks.data.local.entity.SubtaskEntity
import com.example.uthsmarttasks.data.local.entity.TaskEntity
import com.example.uthsmarttasks.viewmodel.TaskViewModel
import com.example.uthsmarttasks.utils.dichDoUuTien
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Add

@Composable
fun DetailScreen(
    navController: NavController,
    taskId: Int,
    viewModel: TaskViewModel = viewModel()
) {
    var task by remember { mutableStateOf<TaskEntity?>(null) }
    var subtasks by remember { mutableStateOf<List<SubtaskEntity>>(emptyList()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showAddSubtaskDialog by remember { mutableStateOf(false) }
    var newSubtaskTitle by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Load task and subtasks
    LaunchedEffect(taskId) {
        task = viewModel.getTaskById(taskId)
    }

    LaunchedEffect(taskId) {
        viewModel.getSubtasksForTask(taskId).collect {
            subtasks = it
        }
    }

    fun toggleSubtask(subtask: SubtaskEntity) {
        viewModel.updateSubtask(subtask.copy(isCompleted = !subtask.isCompleted))
    }

    fun deleteTask() {
        task?.let {
            viewModel.deleteTask(it)
            navController.popBackStack()
        }
    }

    fun addSubtask() {
        if (newSubtaskTitle.isNotBlank() && task != null) {
            val subtask = SubtaskEntity(
                taskId = task!!.id,
                title = newSubtaskTitle,
                isCompleted = false
            )
            viewModel.addSubtask(subtask)
            newSubtaskTitle = ""
            showAddSubtaskDialog = false
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

                Spacer(modifier = Modifier.weight(1f))
                Text("Chi tiết", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { showAddSubtaskDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm công việc phụ", tint = Color.Blue)
                }
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Red)
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (task != null) {
                Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
                    Text(task!!.title, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Badge(containerColor = Color(0xFFE3F2FD)) {
                            Text(task!!.category, modifier = Modifier.padding(4.dp))
                        }
                        Badge(containerColor = if (task!!.priority == "High") Color(0xFFFFEBEE) else Color(0xFFE8F5E9)) {
                            Text(
                                dichDoUuTien(task!!.priority),
                                color = if (task!!.priority == "High") Color.Red else Color.Green,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Mô tả:", fontWeight = FontWeight.Bold)
                    Text(task!!.description, color = Color.Gray)

                    // Subtasks
                    if (subtasks.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Công việc phụ:", fontWeight = FontWeight.Bold)

                        subtasks.forEach { sub ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { toggleSubtask(sub) }
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = sub.isCompleted,
                                    onCheckedChange = { toggleSubtask(sub) },
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
                }
            } else {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Không tìm thấy công việc!", color = Color.Red, textAlign = TextAlign.Center)
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc muốn xóa công việc này không?") },
            confirmButton = {
                Button(
                    onClick = {
                        deleteTask()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Xóa", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }


    if (showAddSubtaskDialog) {
        AlertDialog(
            onDismissRequest = { showAddSubtaskDialog = false },
            title = { Text("Thêm công việc phụ") },
            text = {
                OutlinedTextField(
                    value = newSubtaskTitle,
                    onValueChange = { newSubtaskTitle = it },
                    label = { Text("Tên công việc phụ") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = { addSubtask() },
                    enabled = newSubtaskTitle.isNotBlank()
                ) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubtaskDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
