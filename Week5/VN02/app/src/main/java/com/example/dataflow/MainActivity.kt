package com.example.dataflow

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            // Cấu hình Navigation
            NavHost(navController = navController, startDestination = "screen1") {

                // s1: QMK
                composable(
                    route = "screen1?data={data}",
                    arguments = listOf(navArgument("data") {
                        type = NavType.StringType
                        nullable = true
                    })
                ) { backStackEntry ->
                    val finalData = backStackEntry.arguments?.getString("data")
                    Screen1(navController, finalData)
                }

                // s2: code
                composable("screen2/{email}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    Screen2(navController, email)
                }

                // s3: mkm
                composable("screen3/{email}/{code}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    val code = backStackEntry.arguments?.getString("code") ?: ""
                    Screen3(navController, email, code)
                }

                // s4: xác nhận
                composable("screen4/{email}/{code}/{pass}") { backStackEntry ->
                    val email = backStackEntry.arguments?.getString("email") ?: ""
                    val code = backStackEntry.arguments?.getString("code") ?: ""
                    val pass = backStackEntry.arguments?.getString("pass") ?: ""
                    Screen4(navController, email, code, pass)
                }
            }
        }
    }
}

// s1: nhập email
@Composable
fun Screen1(navController: NavController, finalData: String?) {
    var email by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        UTHHeader("Quên mật khẩu?", "Nhập địa chỉ email của bạn, chúng tôi sẽ gửi mã xác nhận cho bạn.", showBack = false)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email của bạn") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotEmpty()) navController.navigate("screen2/$email")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Kế tiếp")
        }

        if (finalData != null) {
            Spacer(modifier = Modifier.height(30.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dữ liệu trả về:", fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(finalData)
                }
            }
        }
    }
}

// s2: nhập code
@Composable
fun Screen2(navController: NavController, email: String) {
    var code by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        UTHHeader("Xác minh mã", "Nhập mã chúng tôi đã gửi đến $email", onBack = { navController.popBackStack() })

        Text("Nhập mã 6 chữ số", modifier = Modifier.padding(bottom = 16.dp), color = Color.Gray)
        OtpInputField(
            otpText = code,
            onOtpChange = { if (it.length <= 6) code = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (code.length == 6) {
                    navController.navigate("screen3/$email/$code")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Kế tiếp")
        }
    }
}

// s3: tạo mkm
@Composable
fun Screen3(navController: NavController, email: String, code: String) {
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        UTHHeader("Tạo mật khẩu mới", "Nhập mật khẩu mới của bạn", onBack = { navController.popBackStack() })

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu mới") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (password.isNotEmpty()) navController.navigate("screen4/$email/$code/$password")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Kế tiếp")
        }
    }
}

// s4: màn xác nhận
@Composable
fun Screen4(navController: NavController, email: String, code: String, pass: String) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        UTHHeader("Xác nhận", "Chúng tôi luôn sẵn sàng hỗ trợ bạn!", onBack = { navController.popBackStack() })

        OutlinedTextField(value = email, onValueChange = {}, label = { Text("Email") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = code, onValueChange = {}, label = { Text("Code") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = pass, onValueChange = {}, label = { Text("Mật khẩu") }, readOnly = true, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val rawData = "Email: $email\nCode: $code\nPass: $pass"
                val encodedData = Uri.encode(rawData)
                navController.navigate("screen1?data=$encodedData") {
                    popUpTo("screen1") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Nộp")
        }
    }
}

// 1. logo và tiêu đề
@Composable
fun UTHHeader(title: String, subtitle: String, showBack: Boolean = true, onBack: () -> Unit = {}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            if (showBack) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF2196F3))
                }
            }
        }

        Text(text = "UTH", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color(0xFF009688))
        Text(text = "SmartTasks", fontSize = 20.sp, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = subtitle, fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 32.dp))
    }
}

// 2. code 6 số
@Composable
fun OtpInputField(otpText: String, onOtpChange: (String) -> Unit) {
    BasicTextField(
        value = otpText,
        onValueChange = onOtpChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(6) { index ->
                    val char = if (index < otpText.length) otpText[index].toString() else ""

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .border(
                                width = if (char.isNotEmpty()) 2.dp else 1.dp,
                                color = if (char.isNotEmpty()) Color(0xFF2196F3) else Color.LightGray,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}
