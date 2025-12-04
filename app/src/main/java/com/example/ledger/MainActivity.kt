package com.example.ledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransactionApp()
        }
    }
}

@Composable
fun TransactionApp(vm: TransactionViewModel = viewModel()) {

    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    var showBudgetDialog by remember { mutableStateOf(false) }
    var budgetInput by remember { mutableStateOf("") }

    val data = vm.transactions.value
    val budget = vm.budget.value

    val totalSpent = data.sumOf { it.amount }
    val remaining = budget - totalSpent

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text("Budgeting App", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(20.dp))

        // -------------------------
        // SECTION: BUDGET SUMMARY
        // -------------------------
        Text("Total Budget: Rp $budget", style = MaterialTheme.typography.titleMedium)
        Text("Total Spent: Rp $totalSpent", style = MaterialTheme.typography.bodyMedium)
        Text("Remaining: Rp $remaining", style = MaterialTheme.typography.bodyLarge)

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = { showBudgetDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set Budget")
        }

        Spacer(Modifier.height(25.dp))

        // -------------------------
        // INPUT TRANSACTION
        // -------------------------
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        Button(
            onClick = {
                if (title.isNotEmpty() && amount.isNotEmpty()) {
                    vm.add(title, amount.toInt())
                    title = ""
                    amount = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Transaction")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // -------------------------
        // LIST TRANSAKSI
        // -------------------------
        Text("Transaction List:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        data.forEach {
            Text("• ${it.title} - Rp ${it.amount}")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text("Spending Graph:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(10.dp))

        TransactionLineChart(
            modifier = Modifier
                .height(250.dp)
                .fillMaxWidth(),
            data = data
        )
    }

    // -----------------------------------------------------
    // POP-UP DIALOG UNTUK SETTING BUDGET
    // -----------------------------------------------------
    if (showBudgetDialog) {
        AlertDialog(
            onDismissRequest = { showBudgetDialog = false },
            confirmButton = {
                Button(onClick = {
                    if (budgetInput.isNotEmpty()) {
                        vm.setBudget(budgetInput.toInt())
                        budgetInput = ""
                        showBudgetDialog = false
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showBudgetDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Set Budget") },
            text = {
                OutlinedTextField(
                    value = budgetInput,
                    onValueChange = { budgetInput = it },
                    label = { Text("Masukkan Total Budget") }
                )
            }
        )
    }
}
