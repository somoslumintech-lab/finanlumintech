package com.financeapp

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    bottomPadding: PaddingValues,
    onBack: () -> Unit,
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

    LaunchedEffect(Unit) {
        transactions = FinanceStore.getTransactions(context)
    }

    val sortedTransactions = transactions
        .sortedByDescending { it.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Histórico",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->

        if (sortedTransactions.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(bottomPadding),
                contentAlignment = Alignment.Center
            ) {

                EmptyHistory()
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {

                    Text(
                        text = "${sortedTransactions.size} movimentações",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }

                items(
                    items = sortedTransactions,
                    key = { it.id }
                ) { transaction ->

                    HistoryTransactionItem(
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

private fun formatHistoryCurrency(
    value: Double
): String {
    return NumberFormat
        .getCurrencyInstance(
            Locale("pt", "BR")
        )
        .format(value)
}

@Composable
private fun HistoryTransactionItem(
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

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 14.dp,
                    top = 12.dp,
                    bottom = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                modifier = Modifier.size(46.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.background
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
                modifier = Modifier
                    .weight(1f)
            ) {

                Text(
                    text = transaction.description,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = transaction.category,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = "$amountPrefix ${
                        formatHistoryCurrency(
                            transaction.amount
                        )
                    }",
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Row {

                    TextButton(
                        onClick = onEdit
                    ) {
                        Text("Editar")
                    }

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
        }
    }
}

@Composable
private fun EmptyHistory() {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Nenhuma movimentação",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Suas movimentações aparecerão aqui.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}