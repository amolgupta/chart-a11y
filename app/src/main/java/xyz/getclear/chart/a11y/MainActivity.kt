package xyz.getclear.chart.a11y

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.content.ContextCompat
import com.github.lib.ChartAudioImpl
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class MainActivity : AppCompatActivity() {

    val chartAudio by lazy {
        ChartAudioImpl()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<LineChart>(R.id.chart).apply {
            // Configure the chart
            xAxis.setDrawGridLines(false)
            axisRight.setDrawAxisLine(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            setTouchEnabled(true)
            isDragEnabled = true

            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {
                    chartAudio.dispose()
                }

                override fun onValueSelected(e: Entry?, h: Highlight?) {
                    e?.let {
                        chartAudio.onPointFocused(it.y.toDouble())
                    }
                }
            })

            data = getDataSet()
            chartAudio.setRange(getRange())
        }

        findViewById<Button>(R.id.button).setOnClickListener {
            chartAudio.playSummary(sampleData.map { it.y.toDouble() })
        }
    }

    private fun getDataSet(): LineData {
        val dataSet = LineDataSet(sampleData, "Label")
        dataSet.color = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        dataSet.setDrawValues(false)
        return LineData(dataSet)

    }

    private fun getRange(): Pair<Double, Double> {
        val entries = sampleData
        val min = entries.minBy { entry -> entry.y }?.x!!.toDouble()
        val max = entries.maxBy { entry -> entry.y }?.x!!.toDouble()
        return Pair(min, max)
    }
}