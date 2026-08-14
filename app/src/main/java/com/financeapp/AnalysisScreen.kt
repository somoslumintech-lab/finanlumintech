package com.financeapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Calendar
import java.util.Locale

private enum class AnalysisPeriod(
    val label: String
) {
    THIS_MONTH("Este mês"),
    PREVIOUS_MONTH("Mês anterior"),
    LAST_THREE_MONTHS("Últimos 3 meses"),
    ALL_TIME("Todo o período")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    bottomPadding: PaddingValues,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var transactions by remember {
        mutableStateOf(
            FinanceStore.getTransactions(context)
        )
    }

    var selectedPeriod by remember {
        mutableStateOf(
            AnalysisPeriod.THIS_MONTH
        )
    }

    var periodExpanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        transactions = FinanceStore.getTransactions(context)
    }

    val filteredTransactions = filterTransactionsByPeriod(
        transactions = transactions,
        period = selectedPeriod
    )

    val income = filteredTransactions
        .filter {
            it.type == TransactionType.INCOME
        }
        .sumOf {
            it.amount
        }

    val expenses = filteredTransactions
        .filter {
            it.type == TransactionType.EXPENSE
        }
        .sumOf {
            it.amount
        }

    val savings = income - expenses

    val expenseCategories = filteredTransactions
        .filter {
            it.type == TransactionType.EXPENSE
        }
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

    val totalExpenses = expenses

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Análise",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(bottomPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Resumo financeiro",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Visão geral das suas finanças",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            ExposedDropdownMenuBox(
                expanded = periodExpanded,
                onExpandedChange = {
                    periodExpanded = !periodExpanded
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = selectedPeriod.label,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    label = {
                        Text("Período")
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = periodExpanded
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = periodExpanded,
                    onDismissRequest = {
                        periodExpanded = false
                    }
                ) {

                    AnalysisPeriod.values().forEach { period ->

                        DropdownMenuItem(
                            text = {
                                Text(period.label)
                            },
                            onClick = {
                                selectedPeriod = period
                                periodExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                CompactSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Entradas",
                    value = formatAnalysisCurrency(income)
                )

                CompactSummaryCard(
                    modifier = Modifier.weight(1f),
                    title = "Saídas",
                    value = formatAnalysisCurrency(expenses)
                )
            }

            BalanceAnalysisCard(
                value = savings
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Categorias",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (expenseCategories.isEmpty()) {

                Text(
                    text = "Nenhum gasto registrado neste período.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

            } else {

                expenseCategories.forEach { (name, value) ->

                    val percentage =
                        if (totalExpenses > 0) {
                            (value / totalExpenses) * 100
                        } else {
                            0.0
                        }

                    AnalysisCategory(
                        name = name,
                        percentage = String.format(
                            Locale("pt", "BR"),
                            "%.1f%%",
                            percentage
                        ),
                        value = formatAnalysisCurrency(value),
                        progress = if (totalExpenses > 0) {
                            (value / totalExpenses).toFloat()
                        } else {
                            0f
                        }
                    )
                }
            }
        }
    }
}

private fun filterTransactionsByPeriod(
    transactions: List<Transaction>,
    period: AnalysisPeriod
): List<Transaction> {

    if (period == AnalysisPeriod.ALL_TIME) {
        return transactions
    }

    val now = Calendar.getInstance()

    val start = Calendar.getInstance().apply {
        timeInMillis = now.timeInMillis

        set(
            Calendar.DAY_OF_MONTH,
            1
        )

        set(
            Calendar.HOUR_OF_DAY,
            0
        )

        set(
            Calendar.MINUTE,
            0
        )

        set(
            Calendar.SECOND,
            0
        )

        set(
            Calendar.MILLISECOND,
            0
        )

        when (period) {

            AnalysisPeriod.THIS_MONTH -> {
                // Primeiro dia do mês atual.
            }

            AnalysisPeriod.PREVIOUS_MONTH -> {
                add(
                    Calendar.MONTH,
                    -1
                )
            }

            AnalysisPeriod.LAST_THREE_MONTHS -> {
                add(
                    Calendar.MONTH,
                    -2
                )
            }

            AnalysisPeriod.ALL_TIME -> {
                // Tratado acima.
            }
        }
    }

    val startMillis = start.timeInMillis
    val endMillis = now.timeInMillis

    return transactions.filter { transaction ->

        transaction.date in startMillis..endMillis
    }
}

private fun formatAnalysisCurrency(
    value: Double
): String {
    return NumberFormat
        .getCurrencyInstance(
            Locale("pt", "BR")
        )
        .format(value)
}

@Composable
private fun CompactSummaryCard(
    modifier: Modifier,
    title: String,
    value: String
) {

    Card(
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            18.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BalanceAnalysisCard(
    value: Double
) {

    val isPositive = value >= 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(
            20.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isPositive) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = "Saldo",
                    color = if (isPositive) {
                        MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.75f
                        )
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = if (isPositive) {
                        "Você está no positivo"
                    } else {
                        "Você está no negativo"
                    },
                    color = if (isPositive) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = formatAnalysisCurrency(value),
                color = if (isPositive) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onErrorContainer
                },
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun AnalysisCategory(
    name: String,
    percentage: String,
    value: String,
    progress: Float
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = name,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = percentage,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            Text(
                text = value,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        LinearProgressIndicator(
            progress = progress.coerceIn(0f, 1f),
            modifier = Modifier.fillMaxWidth()
        )
    }
}