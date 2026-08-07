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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    bottomPadding: PaddingValues,
    onBack: () -> Unit
) {

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
                text = "Resumo do mês",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Agosto 2026",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SummaryAnalysisCard(
                title = "Entradas",
                value = "R$ 5.200,00"
            )

            SummaryAnalysisCard(
                title = "Saídas",
                value = "R$ 3.180,00"
            )

            SummaryAnalysisCard(
                title = "Economia",
                value = "R$ 2.020,00"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Categorias",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            AnalysisCategory(
                name = "Casa",
                percentage = "29,8%",
                value = "R$ 950,00"
            )

            AnalysisCategory(
                name = "Alimentação",
                percentage = "19,5%",
                value = "R$ 620,00"
            )

            AnalysisCategory(
                name = "Transporte",
                percentage = "12,0%",
                value = "R$ 380,00"
            )

            AnalysisCategory(
                name = "Outros",
                percentage = "38,7%",
                value = "R$ 1.230,00"
            )
        }
    }
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
    value: String
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
}