package com.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current

    var transactions by remember {
        mutableStateOf(
            FinanceStore.getTransactions(context)
        )
    }

    LaunchedEffect(Unit) {
        transactions = FinanceStore.getTransactions(context)
    }

    val income = transactions
        .filter { it.type == TransactionType.INCOME }
        .sumOf { it.amount }

    val expenses = transactions
        .filter { it.type == TransactionType.EXPENSE }
        .sumOf { it.amount }

    val balance = income - expenses

    Scaffold { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                HomeHeader()
            }

            item {
                BalanceCard(balance)
            }

            item {
                FinancialSummary(
                    income = income,
                    expenses = expenses
                )
            }

            item {
                SectionHeader(
                    "Gastos por categoria",
                    "Ver tudo"
                )
            }

            item {
                CategoryList(
                    transactions = transactions
                )
            }

            item {
                SectionHeader(
                    "Movimentações recentes",
                    "Ver tudo"
                )
            }

            if (transactions.isEmpty()) {

                item {
                    EmptyTransactions()
                }

            } else {

                items(
                    transactions.take(10),
                    key = { it.id }
                ) { transaction ->

                    TransactionItem(
                        transaction
                    )
                }
            }
        }
    }
}

private fun formatCurrency(
    value: Double
): String {
    return NumberFormat
        .getCurrencyInstance(
            Locale("pt", "BR")
        )
        .format(value)
}

@Composable
private fun HomeHeader() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            Text(
                text = "Olá 👋",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Visão geral",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        IconButton(
            onClick = {}
        ) {
            Icon(
                Icons.Default.NotificationsNone,
                contentDescription = "Notificações"
            )
        }
    }
}

@Composable
private fun BalanceCard(
    balance: Double
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                "Saldo disponível",
                color = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.75f
                ),
                fontSize = 14.sp
            )

            Spacer(
                Modifier.height(8.dp)
            )

            Text(
                formatCurrency(balance),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                Modifier.height(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    Modifier.width(6.dp)
                )

                Text(
                    "Saldo atual",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun FinancialSummary(
    income: Double,
    expenses: Double
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SummaryCard(
            Modifier.weight(1f),
            "Entradas",
            formatCurrency(income),
            "+"
        )

        SummaryCard(
            Modifier.weight(1f),
            "Saídas",
            formatCurrency(expenses),
            "-"
        )
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier,
    title: String,
    value: String,
    symbol: String
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            Modifier.padding(16.dp)
        ) {

            Text(
                title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )

            Spacer(
                Modifier.height(6.dp)
            )

            Text(
                "$symbol $value",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            action,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight