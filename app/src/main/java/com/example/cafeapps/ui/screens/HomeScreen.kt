package com.example.cafeapps.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cafeapps.data.local.entity.Member
import com.example.cafeapps.ui.CoffeeViewModel
import com.example.cafeapps.ui.theme.CoffeeDark
import com.example.cafeapps.ui.theme.CoffeeMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CoffeeViewModel,
    onAddMemberClick: () -> Unit,
    onMemberClick: (Int) -> Unit
) {
    val members by viewModel.members.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kopi Ukas Members", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CoffeeDark)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMemberClick,
                containerColor = CoffeeMedium,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Member")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = CoffeeDark)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Total Members", color = Color.White.copy(alpha = 0.7f))
                    Text(
                        "${members.size}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                "Member List",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (members.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No members registered yet.", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(members) { member ->
                        MemberItem(member, onClick = { onMemberClick(member.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun MemberItem(member: Member, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = CoffeeMedium.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.padding(12.dp),
                    tint = CoffeeMedium
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(member.name, fontWeight = FontWeight.Bold)
                Text(member.phone, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("${member.points}", fontWeight = FontWeight.Bold, color = CoffeeMedium)
                Text("Points", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}
