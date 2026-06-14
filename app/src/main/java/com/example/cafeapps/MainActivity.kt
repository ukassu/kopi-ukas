package com.example.cafeapps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cafeapps.data.local.AppDatabase
import com.example.cafeapps.data.repository.CoffeeRepository
import com.example.cafeapps.ui.CoffeeApp
import com.example.cafeapps.ui.CoffeeViewModel
import com.example.cafeapps.ui.CoffeeViewModelFactory
import com.example.cafeapps.ui.theme.CafeAppsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val database = AppDatabase.getDatabase(this)
        val repository = CoffeeRepository(database.memberDao(), database.transactionDao())
        val factory = CoffeeViewModelFactory(repository)

        setContent {
            CafeAppsTheme {
                val viewModel: CoffeeViewModel = viewModel(factory = factory)
                CoffeeApp(viewModel)
            }
        }
    }
}
