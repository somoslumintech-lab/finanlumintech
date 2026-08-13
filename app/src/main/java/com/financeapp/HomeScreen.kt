package com.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    bottomPadding: PaddingValues,
    onEdit: (Transaction) -> Unit
) {
    val context = LocalContext.current

    var transactions by remember {
        mutableStateOf(
            FinanceStore.getTransactions(context)
        )
    }

    var transactionToDelete by remember {
        mutableStateOf<Transaction?>(null)
    }

    var showAllCategories by remember {
        mutableStateOf(false)
    }

    var showAllTransactions by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        transactions = FinanceStore.getTransactions(context)
    }

    val income = transactions
        .filter {
            it.type == TransactionType.INCOME
        }
        .sumOf {
            it.amount
        }

    val expenses = transactions
        .filter {
            it.type == TransactionType.EXPENSE
        }
        .sumOf {
            it.amount
        }

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
                BalanceCard(
                    balance = balance
                )
            }

            item {
                FinancialSummary(
                    income = income,
                    expenses = expenses
                )
            }

            item {
                SectionHeader(
                    title = "Gastos por categoria",
                    action = if (showAllCategories) {
                        "Mostrar menos"
                    } else {
                        "Ver tudo"
                    },
                    onAction = {
                        showAllCategories = !showAllCategories
                    }
                )
            }

            item {
                CategoryList(
                    transactions = transactions,
                    showAll = showAllCategories
                )
            }

            item {
                SectionHeader(
                    title = "Movimentações recentes",
                    action = if (showAllTransactions) {
                        "Mostrar menos"
                    } else {
                        "Ver tudo"
                    },
                    onAction = {
                        showAllTransactions = !showAllTransactions
                    }
                )
            }

            if (transactions.isEmpty()) {

                item {
                    EmptyTransactions()
                }

            } else {

                val displayedTransactions =
                    if (showAllTransactions) {
                        transactions
                    } else {
                        transactions.take(10)
                    }

                items(
                    items = displayedTransactions,
                    key = { it.id }
                ) { transaction ->

                    TransactionItem(
                        transaction = transaction,
                        onEdit = {
                            onEdit(transaction)
                        },
                        onDelete = {
                            transactionToDelete = transaction
                        }
                    )
                }
            }
        }
    }

    transactionToDelete?.let { transaction ->

        AlertDialog(
            onDismissRequest = {
                transactionToDelete = null
            },
            title = {
                Text("Excluir movimentação?")
            },
            text = {
                Text(
                    "A movimentação \"${transaction.description}\" será excluída permanentemente."
                )
            },
            confirmButton = {

                TextButton(
                    onClick = {

                        FinanceStore.deleteTransaction(
                            context = context,
                            transactionId = transaction.id
                        )

                        transactions =
                            FinanceStore.getTransactions(context)

                        transactionToDelete = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        transactionToDelete = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
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
                imageVector = Icons.Default.NotificationsNone,
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
                text = "Saldo disponível",
                color = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.75f
                ),
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = formatCurrency(balance),
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.width(6.dp)
                )

                Text(
                    text = "Saldo atual",
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
            modifier = Modifier.weight(1f),
            title = "Entradas",
            value = formatCurrency(income),
            symbol = "+"
        )

        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "Saídas",
            value = formatCurrency(expenses),
            symbol = "-"
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
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "$symbol $value",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    action: String,
    onAction: () -> Unit = {}
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = action,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable {
                onAction()
            }
        )
    }
}

@Composable
private fun CategoryList(
    transactions: List<Transaction>,
    showAll: Boolean
) {

    val expenses = transactions.filter {
        it.type == TransactionType.EXPENSE
    }

    if (expenses.isEmpty()) {

        Text(
            text = "Nenhum gasto registrado ainda.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        return
    }

    val categories = expenses
        .groupBy {
            it.category
        }
        .mapValues { (_, items) ->
            items.sumOf {
                it.amount
            }
        }
        .toList()
        .sortedByDescending {
            it.second
        }

    val total = expenses.sumOf {
        it.amount
    }

    val displayedCategories =
        if (showAll) {
            categories
        } else {
            categories.take(5)
        }

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        displayedCategories.forEach { (name, value) ->

            val progress =
                if (total > 0) {
                    (value / total).toFloat()
                } else {
                    0f
                }

            CategoryItem(
                name = name,
                value = formatCurrency(value),
                progress = progress
            )
        }
    }
}

@Composable
private fun CategoryItem(
    name: String,
    value: String,
    progress: Float
) {

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = name
            )

            Text(
                text = value,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                )
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(
                        progress.coerceIn(0f, 1f)
                    )
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}

@Composable
private fun EmptyTransactions() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Nenhuma movimentação ainda",
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Adicione sua primeira movimentação.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TransactionItem(
    transaction: Transaction,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    val icon = if (
        transaction.type == TransactionType.INCOME
    ) {
        "💰"
    } else {
        "💸"
    }

    val amountPrefix = if (
        transaction.type == TransactionType.INCOME
    ) {
        "+"
    } else {
        "-"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEdit()
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(46.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = icon,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = transaction.description,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = transaction.category,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Text(
            text = "$amountPrefix ${formatCurrency(transaction.amount)}",
            fontWeight = FontWeight.SemiBold,
            color = if (
                transaction.type == TransactionType.INCOME
            ) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )

        IconButton(
            onClick = onDelete
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Excluir movimentação"
            )
        }
    }
}