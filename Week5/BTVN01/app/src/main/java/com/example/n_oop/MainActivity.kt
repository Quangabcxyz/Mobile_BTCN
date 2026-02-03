package com.example.n_oop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController, startDestination = "splash") {
                // màn hình chờ
                composable("splash") {
                    SplashScreen(navController)
                }
                // màn hình giới thiệu
                composable("intro/{index}") { backStackEntry ->
                    val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0
                    OnboardingScreen(navController, index)
                }
            }
        }
    }
}

@Composable
// màn hình chờ
fun SplashScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.uth),
            contentDescription = "Logo UTH"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "UTH SmartTasks",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4285F4)
        )
    }
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("intro/0") {
            popUpTo("splash") { inclusive = true }
        }
    }
}

@Composable
// màn hình giới thiệu
fun OnboardingScreen(navController: NavController, index: Int) {
    val page = onboardingPages.getOrElse(index) { onboardingPages[0] }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // hình ảnh
            androidx.compose.material3.Icon(
                imageVector = page.icon,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 32.dp),
                tint = androidx.compose.ui.graphics.Color(0xFF2196F3)
            )
            Text(
                text = page.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = page.description,
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nút back
            if (index > 0) {
                androidx.compose.material3.IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    androidx.compose.material3.Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp))
            }
            // Chấm tròn
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(onboardingPages.size) { iteration ->
                    val color = if (index == iteration) androidx.compose.ui.graphics.Color(0xFF2196F3) else androidx.compose.ui.graphics.Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }
            // nút next
            androidx.compose.material3.Button(
                onClick = {
                    if (index < onboardingPages.size - 1) {
                        navController.navigate("intro/${index + 1}")
                    } else {
                        navController.navigate("splash") {
                            popUpTo("intro/0") { inclusive = true }
                        }
                    }
                },
                colors = androidx.compose.material3.ButtonDefaults.textButtonColors(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Text(
                    text = if (index < onboardingPages.size - 1) "Next" else "Start",
                    color = androidx.compose.ui.graphics.Color(0xFF2196F3),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class OnboardingData(                                                                                  // đóng gói
    val title: String,
    val description: String,
    val icon: ImageVector
)

val onboardingPages = listOf(
    OnboardingData(
        title = "Quản lý thời gian dễ dàng",
        description = "Với phương pháp quản lý dựa trên mức độ ưu tiên và nhiệm vụ hàng ngày, bạn sẽ dễ dàng quản lý và xác định những nhiệm vụ cần được ưu tiên thực hiện trước tiên.",
        icon = Icons.Default.DateRange
    ),
    OnboardingData(
        title = "Tăng hiệu quả công việc",
        description = "Kỹ năng quản lý thời gian và việc xác định những nhiệm vụ quan trọng hơn sẽ giúp bạn có được số liệu thống kê công việc tốt hơn và luôn được cải thiện.",
        icon = Icons.Default.CheckCircle
    ),
    OnboardingData(
        title = "Thông báo nhắc nhở",
        description = "Ưu điểm của ứng dụng này là nó cũng cung cấp lời nhắc nhở để bạn không quên hoàn thành bài tập của mình một cách tốt nhất.",
        icon = Icons.Default.Notifications
    )
)