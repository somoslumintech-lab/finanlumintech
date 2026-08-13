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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

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

    val savings = income - expenses

    val expenseCategories = transactions
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
                text = "Dados atuais",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SummaryAnalysisCard(
                title = "Entradas",
                value = formatAnalysisCurrency(income)
            )

            SummaryAnalysisCard(
                title = "Saídas",
                value = formatAnalysisCurrency(expenses)
            )

            SummaryAnalysisCard(
                title = "Economia",
                value = formatAnalysisCurrency(savings)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Categorias",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (expenseCategories.isEmpty()) {

                Text(
                    text = "Nenhum gasto registrado ainda.",
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
                        progress = (
                            percentage / 100.0
                        ).toFloat()
                    )
                }
            }
        }
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
private fun SummaryAnalysisCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
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
            horizontalArrangement = Arrangement.SpaceBetween
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
            progress = progress,
            modifier = Modifier.fillMaxWidth()
        )
    }
}