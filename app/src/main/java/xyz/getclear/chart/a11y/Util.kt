package xyz.getclear.chart.a11y

import com.github.lib.ChartAudio
import com.github.mikephil.charting.data.Entry

val sampleData = listOf(
    Entry(1.0f, 3.5f),
    Entry(2.0f, 3.6f),
    Entry(3.0f, 3.9f),
    Entry(5.0f, 3.1f),
    Entry(9.0f, 4.1f),
    Entry(12.0f, 4.9f),
    Entry(15.0f, 3.5f),
    Entry(18.0f, 3.5f),
    Entry(20.0f, 3.8f),
    Entry(21.0f, 3.1f),
    Entry(25.0f, 3.2f),
    Entry(32.0f, 5.0f),
    Entry(35.0f, 5.1f),
    Entry(36.0f, 4.0f),
    Entry(38.0f, 4.5f),
    Entry(40.0f, 3.9f),
    Entry(41.0f, 3.1f),
    Entry(45.0f, 3.0f),
    Entry(51.0f, 4.5f)
)


class MockChartAudio : ChartAudio {
    override fun onPointFocused(newY: Double) {
    }

    override fun playSummary(points: List<Double>) {
    }

    override fun setRange(range: Pair<Double, Double>) {
    }

    override fun dispose() {
    }
}
