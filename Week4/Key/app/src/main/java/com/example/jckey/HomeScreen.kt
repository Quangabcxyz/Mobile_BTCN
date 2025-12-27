package com.example.jckey

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            // Thanh tiêu đề màu xanh nhạt
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "UI Components List",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        // Danh sách cuộn (LazyColumn)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp), // Khoảng cách lề
            verticalArrangement = Arrangement.spacedBy(12.dp) // Khoảng cách giữa các item
        ) {
            // Duyệt qua danh sách componentList
            items(componentList) { item ->
                ComponentItem(
                    component = item,
                    onClick = {

                        navController.navigate(Screen.Detail.route + "/${item.name}")
                    }
                )
            }
        }
    }
}

@Composable
fun ComponentItem(component: UiComponent, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically // Căn giữa theo chiều dọc
        ) {
            // 1. Icon bên trái
            Icon(
                imageVector = component.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(16.dp)) // Khoảng trống ở giữa

            // 2. Cột chữ bên phải (Tên + Mô tả)
            Column {
                Text(
                    text = component.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = component.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}