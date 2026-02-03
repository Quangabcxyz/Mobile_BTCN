package com.example.thuvien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.thuvien.ui.theme.ThuVienTheme

class MainActivity : ComponentActivity() {                                  // Kế thừa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThuVienTheme {
                MainLibraryScreen()
            }
        }
    }
}

@Composable
fun MainLibraryScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val books = remember { getLibraryBooks().toMutableStateList() }
    val users = remember { getUsers() }
    var currentUserIndex by remember { mutableIntStateOf(0) }
    val currentUser = users[currentUserIndex]

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Quản lý") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                    label = { Text("DS Sách") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Người dùng") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            when (selectedTab) {
                0 -> DashboardTab(
                    books = books,
                    users = users,
                    currentUser = currentUser,
                    onUserChange = { currentUserIndex = (currentUserIndex + 1) % users.size },
                    onAddBook = { newTitle ->
                        val newId = (books.maxOfOrNull { it.id } ?: 0) + 1
                        books.add(0, Book(newId, newTitle))
                    }
                )
                1 -> BookListTab(books, currentUser, users)
                2 -> EmployeeTab(users)
            }
        }
    }
}

@Composable
fun DashboardTab(
    books: MutableList<Book>,
    users: List<User>,
    currentUser: User,
    onUserChange: () -> Unit,
    onAddBook: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var tempBookTitle by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Thêm sách mới") },
            text = {
                OutlinedTextField(
                    value = tempBookTitle,
                    onValueChange = { tempBookTitle = it },
                    label = { Text("Nhập tên sách") }
                )
            },
            confirmButton = {
                Button(onClick = {
                    if (tempBookTitle.isNotBlank()) {
                        onAddBook(tempBookTitle)
                        tempBookTitle = ""
                        showDialog = false
                    }
                }) { Text("Lưu") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Hủy") }
            }
        )
    }

    Column {
        Text(
            text = "Hệ thống quản lý thư viện",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Người dùng:", style = MaterialTheme.typography.labelLarge)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = currentUser.name, style = MaterialTheme.typography.titleLarge, color = Color.Blue)
            Button(onClick = onUserChange) {
                Text("Đổi")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1))
        ) {
            Text("Thêm sách mới")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Danh sách sách:", style = MaterialTheme.typography.titleMedium)
        BookListContent(books, currentUser, users)
    }
}

@Composable
fun BookListTab(books: MutableList<Book>, currentUser: User, users: List<User>) {
    Column {
        Text(text = "Kho sách", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))
        BookListContent(books, currentUser, users)
    }
}

@Composable
fun EmployeeTab(users: List<User>) {
    Column {
        Text(text = "Danh sách nhân viên", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))
        users.forEach { user ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun BookListContent(
    books: MutableList<Book>,
    currentUser: User,
    users: List<User>
) {
    LazyColumn {
        items(books.size) { index ->
            BookItem(                                           // Tất cả sự phức tạp về giao diện đã được trừu tượng hóa (ẩn đi) bên trong Component BookItem.
                book = books[index],
                users = users,
                onBorrowClick = { clickedBook ->
                    val newStatus = if (clickedBook.borrowedById == null) currentUser.id else null
                    books[index] = clickedBook.copy(borrowedById = newStatus)
                }
            )
        }
    }
}

@Composable
fun BookItem(
    book: Book,
    users: List<User>,
    onBorrowClick: (Book) -> Unit) {
    val borrowerName = if (book.borrowedById != null) {
        users.find { it.id == book.borrowedById }?.name
    } else null

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = book.borrowedById != null,
                onCheckedChange = { onBorrowClick(book) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (book.borrowedById != null) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (book.borrowedById != null) Color.Gray else Color.Black
                )
                if (borrowerName != null) {
                    Text(
                        text = "Đang giữ bởi: $borrowerName",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

data class Book(                        // Đóng gói tất cả thông tin đó vào một thực thể duy nhất là Book
    val id: Int,
    val title: String,
    var borrowedById: Int? = null)      //  Đóng gói trạng thái mượn vào trong sách
data class User(
    val id: Int,
    val name: String)

fun getLibraryBooks() = listOf(
    Book(1, "Sách Văn"),
    Book(2, "Sách Sử"),
    Book(3, "Sách Toán"),
    Book(4, "Sách Lý"),
    Book(5, "Sách Địa")
)

fun getUsers() = listOf(
    User(1, "Nguyễn Văn A"),
    User(2, "Trần Thị B"),
    User(3, "Lê Văn C")
)

@Preview(showBackground = true)
@Composable
fun LibraryPreview() {
    ThuVienTheme {
        MainLibraryScreen()
    }
}