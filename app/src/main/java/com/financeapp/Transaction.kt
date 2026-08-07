package com.financeapp

data class Transaction(
    val id: Long,
    val type: TransactionType,
    val amount: Double,
    val description: String,
    val category: String = "Outros",
    val date: Long = System.currentTimeMillis()
)

enum class TransactionType {
    INCOME,
    EXPENSE
}