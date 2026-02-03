package com.example.nhapsotinhtoan
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorScreen()
        }
    }
}

@Composable
fun CalculatorScreen() {
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // ham tinh toan
    fun calculate(operation: String) {
        val n1 = number1.toDoubleOrNull() ?: 0.0
        val n2 = number2.toDoubleOrNull() ?: 0.0
        var tempResult = 0.0

        when (operation) {
            "+" -> tempResult = n1 + n2
            "-" -> tempResult = n1 - n2
            "*" -> tempResult = n1 * n2
            "/" -> {
                if (n2 != 0.0) {
                    tempResult = n1 / n2
                } else {
                    result = "Không thể chia cho 0"
                    return
                }
            }
        }

        // cap nhat ket qua, hien thi so tron
        result = if (tempResult % 1.0 == 0.0) {
            tempResult.toInt().toString()
        } else {
            tempResult.toString()
        }
    }

    // giao dien
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // tieu de
        Text(
            text = "Thực hành 03",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 30.dp, top = 50.dp)
        )

        // nhap o 1
        OutlinedTextField(
            value = number1,
            onValueChange = { number1 = it },
            label = { Text("Nhập số thứ nhất") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // cac nut bam
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            CalculatorButton(text = "+", color = Color(0xFFEF5350)) { calculate("+") } // do
            CalculatorButton(text = "-", color = Color(0xFFFFA726)) { calculate("-") } // cam
            CalculatorButton(text = "*", color = Color(0xFF7E57C2)) { calculate("*") } // tim
            CalculatorButton(text = "/", color = Color(0xFF212121)) { calculate("/") } // den
        }

        Spacer(modifier = Modifier.height(20.dp))

        // nhap o 2
        OutlinedTextField(
            value = number2,
            onValueChange = { number2 = it },
            label = { Text("Nhập số thứ hai") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Ket qua
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Kết quả: ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = result,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CalculatorButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .size(width = 70.dp, height = 60.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorScreen()
}