package com.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FinanceApp()
        }
    }
}

@Composable
fun FinanceApp() {
    MaterialTheme {
        FinanceDashboard()
    }
}

data class Transaction(
    val icon: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val isIncome: Boolean
)

@Composable
fun FinanceDashboard() {

    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    val transactions = listOf(
        Transaction(
            icon = "🛒",
            title = "Mercado",
            subtitle = "Hoje • Alimentação",
            amount = "- R$ 85,90",
            isIncome = false
        ),
        Transaction(
            icon = "☕",
            title = "Café",
            subtitle = "Hoje • Alimentação",
            amount = "- R$ 12,00",
            isIncome = false
        ),
        Transaction(
            icon = "💰",
            title = "Salário",
            subtitle = "Hoje • Entrada",
            amount = "+ R$ 5.200,00",
            isIncome = true
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Início"
                        )
                    },
                    label = {
                        Text("Início")
                    }
                )

                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = { selectedItem = 1 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Análise"
                        )
                    },
                    label = {
                        Text("Análise")
                    }
                )

                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = { selectedItem = 2 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar"
                        )
                    },
                    label = {
                        Text("Adicionar")
                    }
                )

                NavigationBarItem(
                    selected = selectedItem == 3,
                    onClick = { selectedItem = 3 },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.People,
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
                Header()
            }

            item {
                BalanceCard()
            }

            item {
                FinancialSummary()
            }

            item {
                SectionHeader(
                    title = "Gastos por categoria",
                    action = "Ver tudo"
                )
            }

            item {
                CategoryList()
            }

            item {
                SectionHeader(
                    title = "Movimentações recentes",
                    action = "Ver tudo"
                )
            }

            items(transactions) { transaction ->
                TransactionItem(transaction)
            }
        }
    }
}

@Composable
fun Header() {

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
fun BalanceCard() {

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
                text = "R$ 8.420,50",
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
                    text = "12,4% este mês",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun FinancialSummary() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "Entradas",
            value = "R$ 5.200",
            symbol = "+"
        )

        SummaryCard(
            modifier = Modifier.weight(1f),
            title = "Saídas",
            value = "R$ 3.180",
            symbol = "-"
        )
    }
}

@Composable
fun SummaryCard(
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
fun SectionHeader(
    title: String,
    action: String
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
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CategoryList() {

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        CategoryItem(
            name = "Alimentação",
            value = "R$ 620,00",
            progress = 0.72f
        )

        CategoryItem(
            name = "Casa",
            value = "R$ 950,00",
            progress = 0.86f
        )

        CategoryItem(
            name = "Transporte",
            value = "R$ 380,00",
            progress = 0.42f
        )
    }
}

@Composable
fun CategoryItem(
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
                text = name,
                fontSize = 14.sp
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
fun TransactionItem(
    transaction: Transaction
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
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
                    text = transaction.icon,
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
                text = transaction.title,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = transaction.subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Text(
            text = transaction.amount,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.isIncome) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
    }
}