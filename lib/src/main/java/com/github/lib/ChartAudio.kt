package com.github.lib

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.schedule
import kotlin.experimental.and
import kotlin.math.sin

class ChartAudioImpl : ChartAudio {
    companion object {
        const val TIMER_NAME = "Summary"
        const val TIMER_DELAY = 250L
        const val VOLTAGE = 32767
        const val SESSION_ID = 1
        const val MAX_FREQUENCY = 1400
        const val MIN_FREQUENCY = 300
        const val SAMPLE_RATE = 4000
        const val duration = 1
    }

    private lateinit var audioTrack: AudioTrack
    private var minValue: Double = 0.0
    private var maxValue: Double = 0.0

    private var timer: TimerTask? = null
    private var currentYValue = 0.0
    private var summaryIndex = 0

    private fun playToneAtGivenProgress(value: Double) {
        stopAudio()

        audioTrack = AudioTrack(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build(),
            AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                .setSampleRate(SAMPLE_RATE)
                .build(),
            2 * duration * SAMPLE_RATE,
            AudioTrack.MODE_STATIC,
            SESSION_ID
        )
        val sound = getSound(value)
        audioTrack.write(sound, 0, sound.size)
        audioTrack.play()
    }

    private fun getSound(value: Double): ByteArray {
        val numSamples = duration * SAMPLE_RATE
        val generatedSound = ByteArray(2 * numSamples)

        val samplingRate = SAMPLE_RATE / getFrequency(value)

        for (index in 0 until numSamples) {
            val newFrequency = (getNormalizedFrequency(index, samplingRate) * VOLTAGE).toShort()
            generatedSound[index] = (newFrequency and 0x00ff).toByte()
            generatedSound[index] = (newFrequency and (0xff00).toShort()).toInt().ushr(8).toByte()
        }
        return generatedSound
    }

    private fun getNormalizedFrequency(index: Int, samplingRate: Double) = sin(2.0 * Math.PI * index.toDouble() / samplingRate)

    private fun stopAudio() {
        if (::audioTrack.isInitialized && audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
            with(audioTrack) {
                pause()
                flush()
                stop()
                release()
            }
        }
    }

    private fun getFrequency(yValue: Double): Double {
        val progress = (yValue - minValue) / (maxValue - minValue)
        return ((MAX_FREQUENCY - MIN_FREQUENCY) * progress) + MIN_FREQUENCY
    }

    override fun dispose() {
        timer?.cancel()
    }

    override fun playSummary(points: List<Double>) {
        timer?.cancel()
        summaryIndex = 0
        timer = Timer(TIMER_NAME, false).schedule(0, TIMER_DELAY) {
            if (summaryIndex < points.size) {
                playToneAtGivenProgress(points[summaryIndex++])
            }
        }
    }

    override fun onPointFocused(newY: Double) {
        if (currentYValue != newY) {
            playToneAtGivenProgress(newY)
            currentYValue = newY
        }

    }

    override fun setRange(range: Pair<Double, Double>) {
        this.minValue = range.first
        this.maxValue = range.second
    }

}

interface ChartAudio {
    /**
     * Plays complete sound summary of the graph
     */
    fun onPointFocused(newY: Double)

    /**
     * Places sound at the selected value
     */
    fun playSummary(points: List<Double>)

    /**
     * Sets the range of data
     */
    fun setRange(range: Pair<Double, Double>)

    /**
     * Stop all audio and dispose
     */

    fun dispose()
}