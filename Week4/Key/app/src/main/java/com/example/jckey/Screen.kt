package com.example.jckey
sealed class Screen(val route: String) {
    object Welcome : Screen("welcome_screen")

    data object Home : Screen("home_screen")

    data object Detail : Screen("detail_screen")
}