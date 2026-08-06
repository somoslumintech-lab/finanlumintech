package com.financeapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBack: () -> Unit
) {

    var amount by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var isIncome by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Adicionar movimentação",
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
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Nova movimentação",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = amount,
                onValueChange = {
                    amount = it
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

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(
                onClick = {
                    // Futuramente salvará no Supabase.
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar movimentação")
            }
        }
    }
}