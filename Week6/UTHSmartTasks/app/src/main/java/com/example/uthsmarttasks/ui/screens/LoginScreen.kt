package com.example.uthsmarttasks.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.uthsmarttasks.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Suppress("DEPRECATION")
@Composable
fun LoginScreen(navController: NavController) {
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

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
