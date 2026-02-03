package com.example.jckey

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale

// 1. Màn hình
@Composable
fun TextDetail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {             //1. Kiểu cơ bản
        StyleExample(
            label = "Độ đậm",
            demo = {
                Text(text = "Bold Text", fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
        )

        StyleExample(
            label = "Kiểu nghiêng",
            demo = {
                Text(text = "Italic Text", fontStyle = FontStyle.Italic, fontSize = 24.sp)
            }
        )
        StyleExample(
            label = "Đổ bóng",
            demo = {
                Text(
                    text = "Shadow Text",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Black,
                        shadow = Shadow(
                            color = Color.Gray,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                )
            }
        )

        // 2. Font Monospace
        StyleExample(
            label = "Kiểu Monospace",
            demo = {
                Text(
                    text = "print('Hello World')",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFF006400)
                )
            }
        )

        // 3. Highlight
        StyleExample(
            label = "Tô nền",
            demo = {
                Text(
                    text = " Highlighted Text ",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.background(Color.Red)
                )
            }
        )

        // 4. Xử lý văn bản dài
        StyleExample(
            label = "Cắt bớt khi quá dài",
            demo = {
                Text(
                    text = "Đây là một đoạn văn bản rất dài, dài lê thê và sẽ bị cắt bớt bằng dấu ba chấm nếu vượt quá giới hạn dòng.",
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis, // Hiện dấu "..."
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        )

        StyleExample(
            label = "Giãn cách",
            demo = {
                Text(text = "S P A C I N G", fontSize = 20.sp, letterSpacing = 8.sp)
            }
        )

        StyleExample(
            label = "Gạch chân và gạch ngang",
            demo = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Underline Text", textDecoration = TextDecoration.Underline, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Line Through", textDecoration = TextDecoration.LineThrough, fontSize = 20.sp, color = Color.Gray)
                }
            }
        )
    }
}

@Composable
fun StyleExample(label: String, demo: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        demo()
    }
}

// 2. BUTTON
@Composable
fun ButtonDetail() {
    var count by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Số lần bấm: $count",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider()
        StyleExample(
            label = "Nút có chức năng",
            demo = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Button(onClick = { count++ }) {
                        Text("Bấm để tăng (+1)")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = { count = 0 }) {
                        Text("Reset về 0")
                    }
                }
            }
        )

        // 3. Nút có Icon
        StyleExample(
            label = "Button with Icon",
            demo = {
                Button(onClick = { count++ }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add to Cart (+1)")
                }
            }
        )

        // 4. Nút bị khóa
        StyleExample(
            label = "Không bấm được",
            demo = {
                Button(
                    onClick = { count++ },
                    enabled = false
                ) {
                    Text("Disabled Button")
                }
            }
        )
    }
}

// 3. TEXT FIELD
@Composable
fun TextFieldDetail() {
    var textNormal by remember { mutableStateOf("") }
    var textNumber by remember { mutableStateOf("") }
    var textPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var textError by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Ô nhập cơ bản
        StyleExample(
            label = "Cơ bản",
            demo = {
                OutlinedTextField(
                    value = textNormal,
                    onValueChange = { textNormal = it },
                    label = { Text("Full Name") },
                    placeholder = { Text("Enter your name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )

        // 2. Ô nhập pass
        StyleExample(
            label = "Mật khẩu",
            demo = {
                OutlinedTextField(
                    value = textPassword,
                    onValueChange = { textPassword = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Check else Icons.Default.Clear
                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )

        // 3. Ô nhập Số
        StyleExample(
            label = "Chỉ nhập số",
            demo = {
                OutlinedTextField(
                    value = textNumber,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            textNumber = it
                        }
                    },
                    label = { Text("Phone Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )

        // 4. Trạng thái báo lỗi
        StyleExample(
            label = "Báo lỗi",
            demo = {
                OutlinedTextField(
                    value = textError,
                    onValueChange = { textError = it },
                    label = { Text("Email") },
                    isError = textError.isEmpty(),
                    supportingText = {
                        if (textError.isEmpty()) {
                            Text("Field cannot be empty!", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

// 4. Nhóm Hiển thị
@Composable
fun ImageDetail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Ảnh Avatar Tròn
        StyleExample(
            label = "Ảnh tròn + Viền",
            demo = {
                Image(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        )

        // 2. Ảnh Bo góc
        StyleExample(
            label = "Bo góc hiện đại",
            demo = {
                Image(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Cover Image",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE0F7FA))
                        .padding(32.dp)
                )
            }
        )

        // 3. Aspect Ratio
        StyleExample(
            label = "Đổi màu ảnh",
            demo = {
                Image(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    colorFilter = ColorFilter.tint(Color.Red) // Nhuộm đỏ
                )
            }
        )
    }
}

@Composable
fun IconDetail() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StyleExample(
            label = "Kích thước và màu",
            demo = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, Modifier.size(24.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.Star, null, Modifier.size(48.dp), tint = Color(0xFFFFC107))
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(Icons.Default.Star, null, Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
        )

        // 2. IconButton
        StyleExample(
            label = "Nút bấm dạng Icon",
            demo = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Nút Like
                    IconButton(onClick = { /* Xử lý like */ }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Like", tint = Color.Red)
                    }

                    // Nút Share
                    FilledIconButton(onClick = { /* Xử lý share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }

                    // Nút Edit
                    OutlinedIconButton(onClick = { /* Xử lý edit */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                }
            }
        )
    }
}

// 5. Nhóm lựa chọn
@Composable
fun CheckboxDetail() {
    var isChecked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text(if (isChecked) "Đã chọn" else "Chưa chọn")
        }
    }
}

@Composable
fun SwitchDetail() {
    var isToggled by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Switch(
            checked = isToggled,
            onCheckedChange = { isToggled = it }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(if (isToggled) "Trạng thái: BẬT" else "Trạng thái: TẮT")
    }
}

// 6. Nhóm bố cục
@Composable
fun ColumnDetail() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Column: Xếp dọc")
        ColorBox(androidx.compose.ui.graphics.Color.Red)
        ColorBox(androidx.compose.ui.graphics.Color.Green)
        ColorBox(androidx.compose.ui.graphics.Color.Blue)
    }
}

@Composable
fun RowDetail() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Row: Xếp ngang")
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ColorBox(androidx.compose.ui.graphics.Color.Red)
            ColorBox(androidx.compose.ui.graphics.Color.Green)
            ColorBox(androidx.compose.ui.graphics.Color.Blue)
        }
    }
}

@Composable
fun BoxDetail() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Box: Xếp chồng (Layer)", modifier = Modifier.align(Alignment.TopCenter))
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.size(200.dp).background(androidx.compose.ui.graphics.Color.Red)
        )
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.size(140.dp).background(androidx.compose.ui.graphics.Color.Green)
        )
        androidx.compose.foundation.layout.Box(
            modifier = Modifier.size(80.dp).background(androidx.compose.ui.graphics.Color.Blue)
        )
    }
}

@Composable
fun ColorBox(color: androidx.compose.ui.graphics.Color) {
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(80.dp)
            .background(color, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
    )
}

// 7. RadioButton
@Composable
fun RadioButtonDetail() {
    val options = listOf("Buồn", "Vui", "Không vui buồn")
    var selectedOption by remember { mutableStateOf(options[0]) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bạn đang chọn: $selectedOption", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Tạo danh sách RadioButton
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { selectedOption = option }
                )
                Text(text = option, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

// 8. Slider
@Composable
fun SliderDetail() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Giá trị: ${(sliderPosition * 100).toInt()}%")

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it }
        )
    }
}

// 9.ProgressBar
@Composable
fun ProgressDetail() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Circular Progress (Loading xoay)")
        Spacer(modifier = Modifier.height(8.dp))
        CircularProgressIndicator()

        Spacer(modifier = Modifier.height(32.dp))

        Text("Linear Progress (Loading ngang)")
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator()
    }
}