package com.example.ledger

class TransactionRepository(private val db: TransactionDatabaseHelper) {

    fun addTransaction(title: String, amount: Int): Long {
        return db.insertTransaction(title, amount)
    }

    fun loadTransactions(): List<TransactionData> {
        return db.getTransactions()
    }

    fun saveBudget(amount: Int) {
        db.saveBudget(amount)
    }

    fun loadBudget(): Int {
        return db.getBudget()
    }
}
