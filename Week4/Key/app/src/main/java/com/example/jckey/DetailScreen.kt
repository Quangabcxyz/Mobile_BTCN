package com.example.jckey

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, componentName: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "$componentName Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (componentName) {

                "Text" -> TextDetail()
                "Button" -> ButtonDetail()
                "TextField" -> TextFieldDetail()

                "Image" -> ImageDetail()
                "Icon" -> IconDetail()
                "Checkbox" -> CheckboxDetail()
                "Switch" -> SwitchDetail()
                "Column" -> ColumnDetail()
                "Row" -> RowDetail()
                "Box" -> BoxDetail()
                "RadioButton" -> RadioButtonDetail()
                "Slider" -> SliderDetail()
                "Progress" -> ProgressDetail()

                else -> Text(text = "Chưa có demo cho $componentName")
            }
        }
    }
}