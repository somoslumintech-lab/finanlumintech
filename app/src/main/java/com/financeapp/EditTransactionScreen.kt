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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    bottomPadding: PaddingValues,
    transaction: Transaction,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var amount by remember {
        mutableStateOf(
            transaction.amount
                .toString()
                .replace(".", ",")
        )
    }

    var description by remember {
        mutableStateOf(transaction.description)
    }

    var isIncome by remember {
        mutableStateOf(
            transaction.type == TransactionType.INCOME
        )
    }

    var category by remember {
        mutableStateOf(transaction.category)
    }

    var categoryExpanded by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val categories = listOf(
        "Casa",
        "Alimentação",
        "Transporte",
        "Saúde",
        "Lazer",
        "Salário",
        "Outros"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Editar movimentação",
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
                text = "Editar dados",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Valor")
                },
                placeholder = {
                    Text("R$ 0,00")
                },
                singleLine = true
            )

            Text(
                text = "Tipo",
                fontWeight = FontWeight.Medium
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = !isIncome,
                    onClick = {
                        isIncome = false
                    }
                )

                Text("Saída")

                RadioButton(
                    selected = isIncome,
                    onClick = {
                        isIncome = true
                    }
                )

                Text("Entrada")
            }

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Descrição")
                },
                placeholder = {
                    Text("Ex.: Mercado")
                },
                singleLine = true
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Categoria")
                    },
                    readOnly = true,
                    singleLine = true
                )

                DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = {
                        categoryExpanded = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    categories.forEach { item ->

                        DropdownMenuItem(
                            text = {
                                Text(item)
                            },
                            onClick = {
                                category = item
                                categoryExpanded = false
                            }
                        )
                    }
                }

                Button(
                    onClick = {
                        categoryExpanded = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Escolher categoria")
                }
            }

            if (errorMessage.isNotEmpty()) {

                Text(
                    text = errorMessage
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = {

                    val normalizedAmount = amount
                        .replace("R$", "")
                        .replace(" ", "")
                        .replace(".", "")
                        .replace(",", ".")
                        .toDoubleOrNull()

                    when {

                        normalizedAmount == null ||
                            normalizedAmount <= 0 -> {

                            errorMessage =
                                "Digite um valor válido."
                        }

                        description.isBlank() -> {

                            errorMessage =
                                "Digite uma descrição."
                        }

                        else -> {

                            val updatedTransaction =
                                Transaction(
                                    id = transaction.id,
                                    type = if (isIncome) {
                                        TransactionType.INCOME
                                    } else {
                                        TransactionType.EXPENSE
                                    },
                                    amount = normalizedAmount,
                                    description = description.trim(),
                                    category = category,
                                    date = transaction.date
                                )

                            FinanceStore.updateTransaction(
                                context = context,
                                transaction = updatedTransaction
                            )

                            onBack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar alterações")
            }
        }
    }
}