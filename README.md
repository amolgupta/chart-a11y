## Making charts accessible

This library is inspired by the charts feature on Yahoo Finance app.
This library enables you to add audio on any android line chart.

### Installation

To install copy the `ChartAudio.kt` file into your project.

### Usage
For an example usage with `MpAndroidChart` see `MainActivity.kt`

- Step 1:
Initialize a `ChartAudio` object in you class which contains the chart view.

```
    val chartAudio by lazy {
        ChartAudioImpl()
    }
```

- Step 2:
Tell the `chartAudio` object about the range of data it should expect

```
    chartAudio.setRange(Pair(min,max))
```


- Step 3:

Set a touch listener on the chart to play a sound when a particular value is selected

```
     chartAudio.onPointFocused(value)
```

- Optional Step 4:

To play a complete summary, pass the complete data set to this function

```
     chartAudio.playSummary(data)
```

- Step 5:

To play only in accessibility mode, add this check:
```
(getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).isEnabled
```