package com.example.cafeapps.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafeapps.ui.CoffeeViewModel
import com.example.cafeapps.ui.theme.CoffeeDark
import com.example.cafeapps.ui.theme.CoffeeMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    viewModel: CoffeeViewModel,
    memberId: Int,
    onTransactionAdded: (Double) -> Unit,
    onBack: () -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    val amount = amountText.toDoubleOrNull() ?: 0.0
    val pointsEarned = (amount / 10000).toInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Transaction", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoffeeDark)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            OutlinedTextField(
                value = amountText,
                onValueChange = { if (it.all { char -> char.isDigit() }) amountText = it },
                label = { Text("Purchase Amount (Rp)") },
                placeholder = { Text("Example: 50000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("Rp ") }
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = CoffeeMedium.copy(alpha = 0.1f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Points to be earned:", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "$pointsEarned Pts",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoffeeMedium
                    )
                    Text(
                        "1 Point = Rp 10.000",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (amount > 0) {
                        onTransactionAdded(amount)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = amount > 0,
                colors = ButtonDefaults.buttonColors(containerColor = CoffeeMedium),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save Transaction", modifier = Modifier.padding(8.dp))
            }
        }
    }
}
