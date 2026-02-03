package com.example.uthsmarttasks

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.uthsmarttasks.ui.screens.AddTaskScreen
import com.example.uthsmarttasks.ui.screens.DetailScreen
import com.example.uthsmarttasks.ui.screens.HomeScreen
import com.example.uthsmarttasks.ui.screens.LoginScreen
import com.example.uthsmarttasks.ui.screens.ProfileScreen
import com.example.uthsmarttasks.ui.screens.CalendarScreen
import com.example.uthsmarttasks.ui.screens.DocumentScreen
import com.example.uthsmarttasks.ui.screens.SettingsScreen
import com.example.uthsmarttasks.viewmodel.TaskViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val startDestination = if (auth.currentUser != null) "home" else "login"
    val context = LocalContext.current
    
    // ViewModel (MVVM Architecture)
    val viewModel: TaskViewModel = remember {
        TaskViewModel(context.applicationContext as Application)
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { LoginScreen(navController) }
        composable("home") { 
            HomeScreen(navController = navController, viewModel = viewModel) 
        }
        composable("add_task") {
            AddTaskScreen(navController = navController, viewModel = viewModel)
        }
        composable("profile") { ProfileScreen(navController) }
        composable("detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            DetailScreen(
                navController = navController,
                taskId = taskId,
                viewModel = viewModel
            )
        }

        composable("calendar") { CalendarScreen(navController) }
    
        composable("docs") { DocumentScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}