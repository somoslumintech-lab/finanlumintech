package com.financeapp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object FinanceStore {

    private const val PREFS_NAME = "finance_data"
    private const val TRANSACTIONS_KEY = "transactions"

    fun getTransactions(context: Context): List<Transaction> {
        val prefs = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        val json = prefs.getString(
            TRANSACTIONS_KEY,
            "[]"
        ) ?: "[]"

        val array = JSONArray(json)

        val transactions = mutableListOf<Transaction>()

        for (i in 0 until array.length()) {

            val item = array.getJSONObject(i)

            transactions.add(
                Transaction(
                    id = item.getLong("id"),
                    type = TransactionType.valueOf(
                        item.getString("type")
                    ),
                    amount = item.getDouble("amount"),
                    description = item.getString("description"),
                    category = item.optString(
                        "category",
                        "Outros"
                    ),
                    date = item.optLong(
                        "date",
                        System.currentTimeMillis()
                    )
                )
            )
        }

        return transactions.sortedByDescending {
            it.date
        }
    }

    fun addTransaction(
        context: Context,
        transaction: Transaction
    ) {
        val transactions = getTransactions(
            context
        ).toMutableList()

        transactions.add(transaction)

        saveTransactions(
            context,
            transactions
        )
    }

    fun deleteTransaction(
        context: Context,
        transactionId: Long
    ) {
        val transactions = getTransactions(
            context
        ).toMutableList()

        transactions.removeAll {
            it.id == transactionId
        }

        saveTransactions(
            context,
            transactions
        )
    }

    private fun saveTransactions(
        context: Context,
        transactions: List<Transaction>
    ) {
        val array = JSONArray()

        transactions.forEach { transaction ->

            val item = JSONObject()

            item.put(
                "id",
                transaction.id
            )

            item.put(
                "type",
                transaction.type.name
            )

            item.put(
                "amount",
                transaction.amount
            )

            item.put(
                "description",
                transaction.description
            )

            item.put(
                "category",
                transaction.category
            )

            item.put(
                "date",
                transaction.date
            )

            array.put(item)
        }

        context
            .getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )
            .edit()
            .putString(
                TRANSACTIONS_KEY,
                array.toString()
            )
            .apply()
    }
}