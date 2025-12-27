package com.example.jckey

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class UiComponent(
    val name: String,
    val description: String,
    val icon: ImageVector
)
val componentList = listOf(
    // Nhóm Display
    UiComponent("Text", "Displays text", Icons.Default.Info),
    UiComponent("Image", "Displays an image", Icons.Default.AccountBox),
    UiComponent("Icon", "Displays vector icon", Icons.Default.Star),

    // Nhóm Inputs
    UiComponent("Button", "Triggers an action", Icons.Default.Add),
    UiComponent("TextField", "Input field for text", Icons.Default.Edit),
    UiComponent("Checkbox", "Select multiple options", Icons.Default.Check),
    UiComponent("Switch", "Toggle On/Off", Icons.Default.Refresh),

    // Nhóm Layouts
    UiComponent("Column", "Arranges vertically", Icons.Default.List),
    UiComponent("Row", "Arranges horizontally", Icons.Default.Menu),
    UiComponent("Box", "Puts elements on top", Icons.Default.Home),
    UiComponent(name = "RadioButton", description = "Select one option", Icons.Default.CheckCircle),
    UiComponent(name = "Slider", description = "Select value range", Icons.Default.Settings),
    UiComponent(name = "Progress", description = "Loading indicators", Icons.Default.Refresh)
)