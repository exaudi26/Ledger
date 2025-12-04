package com.example.ledger

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class TransactionViewModel(app: Application) : AndroidViewModel(app) {

    private val repo: TransactionRepository

    val transactions = mutableStateOf<List<TransactionData>>(emptyList())
    val budget = mutableStateOf(0)   // <-- DIPINDAH KE ATAS

    init {
        val db = TransactionDatabaseHelper(app)
        repo = TransactionRepository(db)

        load()

        // load budget dari database
        budget.value = repo.loadBudget()
    }

    fun add(title: String, amount: Int) {
        repo.addTransaction(title, amount)
        load()
    }

    fun load() {
        transactions.value = repo.loadTransactions()
    }

    fun setBudget(value: Int) {
        budget.value = value
        repo.saveBudget(value)
    }
}
