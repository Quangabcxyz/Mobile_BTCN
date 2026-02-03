package com.example.thanhtoan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thanhtoan.ui.theme.ThanhToanTheme

class MainActivity : ComponentActivity() {                              // kế thừa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThanhToanTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PaymentScreen()
                }
            }
        }
    }
}

@Composable
fun PaymentScreen() {
    var selectedMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    val methods = getPaymentMethods()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // phần logo
        Spacer(modifier = Modifier.height(50.dp))
        if (selectedMethod == null) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Vui lòng chọn phương thức",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 20.sp
                )
        } else {
            Image(
                painter = painterResource(id = selectedMethod!!.iconResId),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = selectedMethod!!.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(50.dp))

        // phần giữa
        methods.forEach { method ->                                // đa hình
            PaymentRow(
                method = method,
                isSelected = (selectedMethod == method),
                onSelect = { selectedMethod = method }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (selectedMethod != null) {
            Button(
                onClick = { /* Xử lý tiếp theo */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Continue", color = Color.White, fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PaymentRow(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Color.Blue) else null
    ) {
        Row(
            modifier = Modifier.
            padding(16.dp).
            fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = { onSelect() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = method.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(id = method.iconResId),
                contentDescription = method.name,
                modifier = Modifier.size(70.dp)
            )
        }
    }
}

data class PaymentMethod(
    val id: Int,
    val name: String,                        // đóng gói
    val iconResId: Int
)

fun getPaymentMethods(): List<PaymentMethod> {
    return listOf(
        PaymentMethod(1, "PayPal", R.drawable.ic_paypal),
        PaymentMethod(2, "Google Pay", R.drawable.ic_googlepay),
        PaymentMethod(3, "Apple Pay", R.drawable.ic_applepay)
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PaymentScreenPreview() {
    ThanhToanTheme {
        PaymentScreen()
    }
}