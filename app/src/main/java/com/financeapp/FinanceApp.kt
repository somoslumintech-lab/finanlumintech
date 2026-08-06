package com.financeapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun FinanceApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable("analysis") {
            AnalysisScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("add") {
            AddTransactionScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable("group") {
            GroupScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}