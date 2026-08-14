package com.financeapp

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
                onValueChange = { newValue ->

                    val filteredValue = newValue
                        .filter {
                            it.isDigit() ||
                                it == ',' ||
                                it == '.'
                        }

                    val separatorIndex =
                        filteredValue.indexOfFirst {
                            it == ',' || it == '.'
                        }

                    val normalizedValue =
                        if (separatorIndex >= 0) {

                            val integerPart =
                                filteredValue.substring(
                                    0,
                                    separatorIndex
                                )

                            val decimalPart =
                                filteredValue
                                    .substring(
                                        separatorIndex + 1
                                    )
                                    .filter {
                                        it.isDigit()
                                    }
                                    .take(2)

                            integerPart +
                                "," +
                                decimalPart

                        } else {
                            filteredValue
                        }

                    amount = normalizedValue
                    errorMessage = ""
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Valor")
                },
                placeholder = {
                    Text("R$ 0,00")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            Text(
                text = "Tipo",
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                EditTypeButton(
                    text = "Saída",
                    selected = !isIncome,
                    onClick = {
                        isIncome = false
                    },
                    modifier = Modifier.weight(1f)
                )

                EditTypeButton(
                    text = "Entrada",
                    selected = isIncome,
                    onClick = {
                        isIncome = true
                    },
                    modifier = Modifier.weight(1f)
                )
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

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = {
                    categoryExpanded = !categoryExpanded
                },
                modifier = Modifier.fillMaxWidth()
            ) {

                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = {
                        Text("Categoria")
                    },
                    readOnly = true,
                    singleLine = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = categoryExpanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = {
                        categoryExpanded = false
                    }
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
            }

            if (errorMessage.isNotEmpty()) {

                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
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

@Composable
private fun EditTypeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        }

    val textColor =
        if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    Text(
        text = text,
        color = textColor,
        fontWeight = if (selected) {
            FontWeight.Bold
        } else {
            FontWeight.Medium
        },
        modifier = modifier
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                vertical = 14.dp
            ),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}