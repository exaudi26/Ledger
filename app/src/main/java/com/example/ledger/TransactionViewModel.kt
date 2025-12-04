package com.example.ledger

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel

class TransactionViewModel(app: Application) : AndroidViewModel(app){
    private val repo: TransactionRepository

    val transactions = mutableStateOf<List<TransactionData>>(emptyList())

    init {
        val db = TransactionDatabaseHelper(app)
        repo = TransactionRepository(db)
        load()
    }

    fun add(title: String, amount: Int) {
        repo.addTransaction(title, amount)
        load()
    }

    fun load() {
        transactions.value = repo.loadTransactions()
    }
}