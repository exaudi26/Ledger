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

    val data = vm.transactions.value

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "SQLite Testing App", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Amount") }
        )

        Spacer(Modifier.height(10.dp))

        Button(onClick = {
            if (title.isNotEmpty() && amount.isNotEmpty()) {
                vm.add(title, amount.toInt())
                title = ""
                amount = ""
            }
        }) {
            Text("Add Transaction")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Transaction List:")
        Spacer(modifier = Modifier.height(10.dp))

        data.forEach {
            Text("• ${it.title} - ${it.amount}")
        }
    }
}