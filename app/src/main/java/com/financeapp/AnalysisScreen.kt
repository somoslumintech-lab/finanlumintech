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

    val end = Calendar.getInstance().apply {

        when (period) {

            AnalysisPeriod.THIS_MONTH -> {
                timeInMillis = now.timeInMillis
            }

            AnalysisPeriod.PREVIOUS_MONTH -> {

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

                add(
                    Calendar.MILLISECOND,
                    -1
                )
            }

            AnalysisPeriod.LAST_THREE_MONTHS -> {
                timeInMillis = now.timeInMillis
            }

            AnalysisPeriod.ALL_TIME -> {
                timeInMillis = now.timeInMillis
            }
        }
    }

    val startMillis = start.timeInMillis
    val endMillis = end.timeInMillis

    return transactions.filter { transaction ->

        transaction.date in startMillis..endMillis
    }
}