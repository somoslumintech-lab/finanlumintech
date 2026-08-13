package com.financeapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun FinanceApp() {

    var currentScreen by remember {
        mutableStateOf("home")
    }

    var selectedTransaction by remember {
        mutableStateOf<Transaction?>(null)
    }

    Scaffold(
        bottomBar = {

            NavigationBar {

                NavigationBarItem(
                    selected = currentScreen == "home",
                    onClick = {
                        currentScreen = "home"
                    },
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Início"
                        )
                    },
                    label = {
                        Text("Início")
                    }
                )

                NavigationBarItem(
                    selected = currentScreen == "analysis",
                    onClick = {
                        currentScreen = "analysis"
                    },
                    icon = {
                        Icon(
                            Icons.Default.Analytics,
                            contentDescription = "Análise"
                        )
                    },
                    label = {
                        Text("Análise")
                    }
                )

                NavigationBarItem(
                    selected = currentScreen == "add",
                    onClick = {
                        currentScreen = "add"
                    },
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Adicionar"
                        )
                    },
                    label = {
                        Text("Adicionar")
                    }
                )

                NavigationBarItem(
                    selected = currentScreen == "group",
                    onClick = {
                        currentScreen = "group"
                    },
                    icon = {
                        Icon(
                            Icons.Default.People,
                            contentDescription = "Grupo"
                        )
                    },
                    label = {
                        Text("Grupo")
                    }
                )
            }
        }
    ) { padding ->

        when (currentScreen) {

            "home" -> {

                HomeScreen(
                    bottomPadding = padding,
                    onEdit = { transaction ->

                        selectedTransaction = transaction
                        currentScreen = "edit"
                    }
                )
            }

            "analysis" -> {

                AnalysisScreen(
                    bottomPadding = padding,
                    onBack = {
                        currentScreen = "home"
                    }
                )
            }

            "add" -> {

                AddTransactionScreen(
                    bottomPadding = padding,
                    onBack = {
                        currentScreen = "home"
                    }
                )
            }

            "group" -> {

                GroupScreen(
                    bottomPadding = padding,
                    onBack = {
                        currentScreen = "home"
                    }
                )
            }

            "edit" -> {

                selectedTransaction?.let { transaction ->

                    EditTransactionScreen(
                        bottomPadding = padding,
                        transaction = transaction,
                        onBack = {
                            selectedTransaction = null
                            currentScreen = "home"
                        }
                    )
                } ?: run {

                    currentScreen = "home"
                }
            }
        }
    }
}