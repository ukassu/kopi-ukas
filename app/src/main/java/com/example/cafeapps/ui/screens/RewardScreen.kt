package com.example.cafeapps.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafeapps.ui.CoffeeViewModel
import com.example.cafeapps.ui.theme.CoffeeDark
import com.example.cafeapps.ui.theme.CoffeeMedium

data class Reward(val name: String, val points: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardScreen(
    viewModel: CoffeeViewModel,
    memberId: Int,
    onBack: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()
    val rewards = listOf(
        Reward("Espresso", 50),
        Reward("Cappuccino", 100),
        Reward("Latte Gratis", 150)
    )

    var showDialog by remember { mutableStateOf<Reward?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Redeem Rewards", color = Color.White) },
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
        ) {
            member?.let { m ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = CoffeeMedium.copy(alpha = 0.1f)
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Your Current Points", style = MaterialTheme.typography.labelMedium)
                        Text(
                            "${m.points}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = CoffeeMedium
                        )
                    }
                }
            }

            Text(
                "Available Rewards",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(rewards) { reward ->
                    RewardItem(
                        reward = reward,
                        canRedeem = (member?.points ?: 0) >= reward.points,
                        onRedeemClick = { showDialog = reward }
                    )
                }
            }
        }
    }

    showDialog?.let { reward ->
        AlertDialog(
            onDismissRequest = { showDialog = null },
            title = { Text("Confirm Redemption") },
            text = { Text("Are you sure you want to redeem ${reward.points} points for a ${reward.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.redeemReward(memberId, reward.points)
                    showDialog = null
                }) {
                    Text("Redeem")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun RewardItem(reward: Reward, canRedeem: Boolean, onRedeemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CardGiftcard,
                contentDescription = null,
                tint = if (canRedeem) CoffeeMedium else Color.Gray,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(reward.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("${reward.points} Points", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Button(
                onClick = onRedeemClick,
                enabled = canRedeem,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoffeeMedium,
                    disabledContainerColor = Color.LightGray
                ),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text("Redeem", fontSize = 12.sp)
            }
        }
    }
}
