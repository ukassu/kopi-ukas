package com.example.cafeapps.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafeapps.data.local.entity.Transaction
import com.example.cafeapps.ui.CoffeeViewModel
import com.example.cafeapps.ui.theme.CoffeeDark
import com.example.cafeapps.ui.theme.CoffeeMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    viewModel: CoffeeViewModel,
    memberId: Int,
    onBack: () -> Unit,
    onAddTransactionClick: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsState()
    val member by viewModel.selectedMember.collectAsState()

    LaunchedEffect(memberId) {
        viewModel.selectMember(memberId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction History", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoffeeDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransactionClick,
                containerColor = CoffeeMedium,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            member?.let { m ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CoffeeMedium.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(m.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text("ID: MB-00${m.id}", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${m.points}", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = CoffeeMedium)
                            Text("Points", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }

            if (transactions.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No transactions found.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = CoffeeDark,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Rp ${String.format("%,.0f", transaction.amount)}",
                    fontWeight = FontWeight.Bold
                )
                Text(transaction.date, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text(
                "+${transaction.pointEarned} Pts",
                fontWeight = FontWeight.Bold,
                color = CoffeeMedium
            )
        }
    }
}
