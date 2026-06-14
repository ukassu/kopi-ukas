package com.example.cafeapps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cafeapps.ui.screens.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object Profile : Screen("profile")
    object Home : Screen("home")
    object AddMember : Screen("add_member")
    object MemberCard : Screen("member_card/{memberId}") {
        fun createRoute(memberId: Int) = "member_card/$memberId"
    }
    object TransactionHistory : Screen("transaction_history/{memberId}") {
        fun createRoute(memberId: Int) = "transaction_history/$memberId"
    }
    object AddTransaction : Screen("add_transaction/{memberId}") {
        fun createRoute(memberId: Int) = "member_card/$memberId" // Fallback or specific
        fun createTransactionRoute(memberId: Int) = "add_transaction/$memberId"
    }
    object Reward : Screen("reward/{memberId}") {
        fun createRoute(memberId: Int) = "reward/$memberId"
    }
}

@Composable
fun CoffeeApp(viewModel: CoffeeViewModel) {
    val navController = rememberNavController()
    val loggedInMember by viewModel.loggedInMember.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                val destination = if (loggedInMember != null) {
                    Screen.MemberCard.createRoute(loggedInMember!!.id)
                } else {
                    Screen.Login.route
                }
                navController.navigate(destination) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { memberId ->
                    navController.navigate(Screen.MemberCard.createRoute(memberId)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                viewModel = viewModel,
                onRegisterSuccess = { memberId ->
                    navController.navigate(Screen.MemberCard.createRoute(memberId)) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    viewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onAddMemberClick = { navController.navigate(Screen.AddMember.route) },
                onMemberClick = { memberId ->
                    viewModel.selectMember(memberId)
                    navController.navigate(Screen.MemberCard.createRoute(memberId))
                }
            )
        }

        composable(Screen.AddMember.route) {
            AddMemberScreen(
                onMemberAdded = { _, _, _ ->
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MemberCard.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            MemberCardScreen(
                viewModel = viewModel,
                memberId = memberId,
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onTransactionsClick = { navController.navigate(Screen.TransactionHistory.createRoute(memberId) ) },
                onRewardsClick = { navController.navigate(Screen.Reward.createRoute(memberId)) }
            )
        }
        
        composable(
            route = Screen.TransactionHistory.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            TransactionHistoryScreen(
                viewModel = viewModel,
                memberId = memberId,
                onBack = { navController.popBackStack() },
                onAddTransactionClick = { 
                    navController.navigate("add_transaction/$memberId") 
                }
            )
        }
        
        composable(
            route = Screen.AddTransaction.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            AddTransactionScreen(
                viewModel = viewModel,
                memberId = memberId,
                onTransactionAdded = { amount ->
                    viewModel.addTransaction(memberId, amount)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Reward.route,
            arguments = listOf(navArgument("memberId") { type = NavType.IntType })
        ) { backStackEntry ->
            val memberId = backStackEntry.arguments?.getInt("memberId") ?: 0
            RewardScreen(
                viewModel = viewModel,
                memberId = memberId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
