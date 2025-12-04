package com.example.ledger

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun TransactionLineChart(
    modifier: Modifier = Modifier,
    data: List<TransactionData>
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            LineChart(context)
        },
        update = { chart ->
            val entries = data.mapIndexed { index, item ->
                Entry(index.toFloat(), item.amount.toFloat())
            }

            val dataSet = LineDataSet(entries, "Transaction History")
            dataSet.color = Color.BLUE
            dataSet.setCircleColor(Color.BLUE)
            dataSet.lineWidth = 2f
            dataSet.circleRadius = 3f

            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}
