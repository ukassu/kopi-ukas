package com.example.cafeapps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Redeem
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
import com.example.cafeapps.ui.CoffeeViewModel
import com.example.cafeapps.ui.theme.CoffeeAccent
import com.example.cafeapps.ui.theme.CoffeeDark
import com.example.cafeapps.ui.theme.CoffeeMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberCardScreen(
    viewModel: CoffeeViewModel,
    memberId: Int,
    onProfileClick: () -> Unit,
    onTransactionsClick: () -> Unit,
    onRewardsClick: () -> Unit
) {
    val member by viewModel.selectedMember.collectAsState()

    LaunchedEffect(memberId) {
        viewModel.selectMember(memberId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Membership Card", color = Color.White) },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoffeeDark)
            )
        }
    ) { padding ->
        member?.let { m ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Digital Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CoffeeDark)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .padding(24.dp)
                                .fillMaxSize()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "KOPI UKAS",
                                    color = CoffeeAccent,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Text(
                                    "GOLD MEMBER",
                                    color = Color.White.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            
                            Spacer(modifier = Modifier.weight(1f))
                            
                            Text(
                                m.name.uppercase(),
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "ID: MB-00${m.id}",
                                color = Color.White.copy(alpha = 0.6f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Column {
                                    Text("TOTAL POINTS", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                                    Text("${m.points}", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                }
                                Icon(
                                    Icons.Default.QrCode2,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(64.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Menu Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    MenuButton(
                        icon = Icons.Default.History,
                        label = "Transactions",
                        onClick = onTransactionsClick,
                        modifier = Modifier.weight(1f)
                    )
                    MenuButton(
                        icon = Icons.Default.Redeem,
                        label = "Rewards",
                        onClick = onRewardsClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = CoffeeMedium)
        }
    }
}

@Composable
fun MenuButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = CoffeeDark)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontWeight = FontWeight.Medium)
        }
    }
}
