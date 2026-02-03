package com.example.uthsmarttasks.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uthsmarttasks.data.local.entity.TaskEntity
import com.example.uthsmarttasks.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel
) {
    val context = LocalContext.current
    
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Học tập") }
    var selectedPriority by remember { mutableStateOf("Medium") }
    var dueDate by remember { mutableStateOf("") }
    var showCategoryMenu by remember { mutableStateOf(false) }
    var showPriorityMenu by remember { mutableStateOf(false) }
    
    val categories = listOf("Học tập", "Cá nhân", "Công việc", "Khác")
    val priorities = listOf("High", "Medium", "Low")
    
    val calendar = Calendar.getInstance()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    // Initialize with today's date
    LaunchedEffect(Unit) {
        dueDate = dateFormatter.format(calendar.time)
    }
    
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            dueDate = dateFormatter.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    
    fun saveTask() {
        if (title.isBlank()) {
            return
        }
        
        val newTask = TaskEntity(
            title = title,
            description = description.ifBlank { "Không có mô tả" },
            status = "Pending",
            priority = selectedPriority,
            dueDate = dueDate,
            category = selectedCategory
        )
        
        viewModel.addTask(newTask)
        navController.popBackStack()
    }
    
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Thêm Task Mới",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Title
            Text("Tiêu đề *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Nhập tiêu đề công việc") },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Description
            Text("Mô tả", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = { Text("Nhập mô tả chi tiết") },
                maxLines = 5
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category
            Text("Danh mục", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            ExposedDropdownMenuBox(
                expanded = showCategoryMenu,
                onExpandedChange = { showCategoryMenu = it }
            ) {
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoryMenu) }
                )
                ExposedDropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { showCategoryMenu = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                showCategoryMenu = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Priority
            Text("Độ ưu tiên", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                priorities.forEach { priority ->
                    val isSelected = selectedPriority == priority
                    val backgroundColor = when (priority) {
                        "High" -> if (isSelected) Color(0xFFFF5252) else Color(0xFFFFCDD2)
                        "Medium" -> if (isSelected) Color(0xFFFFC107) else Color(0xFFFFF9C4)
                        else -> if (isSelected) Color(0xFF4CAF50) else Color(0xFFC8E6C9)
                    }
                    val textColor = if (isSelected) Color.White else Color.Black
                    
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedPriority = priority },
                        shape = RoundedCornerShape(8.dp),
                        color = backgroundColor
                    ) {
                        Box(
                            modifier = Modifier.padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (priority) {
                                    "High" -> "Cao"
                                    "Medium" -> "Trung bình"
                                    else -> "Thấp"
                                },
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = textColor
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Due Date
            Text("Ngày hết hạn", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = dueDate,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày")
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Add Button
            Button(
                onClick = { saveTask() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = title.isNotBlank()
            ) {
                Text("Thêm Task", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
