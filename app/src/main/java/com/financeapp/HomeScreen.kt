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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Transaction(
    val icon: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val isIncome: Boolean
)

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit
) {
    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    val transactions = listOf(
        Transaction(
            "🛒",
            "Mercado",
            "Hoje • Alimentação",
            "- R$ 85,90",
            false
        ),
        Transaction(
            "☕",
            "Café",
            "Hoje • Alimentação",
            "- R$ 12,00",
            false
        ),
        Transaction(
            "💰",
            "Salário",
            "Hoje • Entrada",
            "+ R$ 5.200,00",
            true
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
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
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        onNavigate("analysis")
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
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        onNavigate("add")
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
                    selected = selectedItem == 3,
                    onClick = {
                        selectedItem = 3
                        onNavigate("group")
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
                BalanceCard()
            }

            item {
                FinancialSummary()
            }

            item {
                SectionHeader(
                    "Gastos por categoria",
                    "Ver tudo"
                )
            }

            item {
                CategoryList()
            }

            item {
                SectionHeader(
                    "Movimentações recentes",
                    "Ver tudo"
                )
            }

            items(transactions) {
                TransactionItem(it)
            }
        }
    }
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
private fun BalanceCard() {

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
                "R$ 8.420,50",
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
                    "12,4% este mês",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun FinancialSummary() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SummaryCard(
            Modifier.weight(1f),
            "Entradas",
            "R$ 5.200",
            "+"
        )

        SummaryCard(
            Modifier.weight(1f),
            "Saídas",
            "R$ 3.180",
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
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CategoryList() {

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        CategoryItem(
            "Alimentação",
            "R$ 620,00",
            0.72f
        )

        CategoryItem(
            "Casa",
            "R$ 950,00",
            0.86f
        )

        CategoryItem(
            "Transporte",
            "R$ 380,00",
            0.42f
        )
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
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(name)

            Text(
                value,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(
            Modifier.height(7.dp)
        )

        Box(
            Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    MaterialTheme.colorScheme.surfaceVariant
                )
        ) {

            Box(
                Modifier
                    .fillMaxWidth(progress)
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
private fun TransactionItem(
    transaction: Transaction
) {

    Row(
        Modifier.fillMaxWidth(),
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
                    transaction.icon,
                    fontSize = 20.sp
                )
            }
        }

        Spacer(
            Modifier.width(12.dp)
        )

        Column(
            Modifier.weight(1f)
        ) {

            Text(
                transaction.title,
                fontWeight = FontWeight.Medium
            )

            Text(
                transaction.subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Text(
            transaction.amount,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.isIncome) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}