package com.johny.weatherc.presentation.ui.screen.main

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johny.weatherc.domain.model.WeatherItem
import com.johny.weatherc.utils.TimeUtil

const val START_X = 100f
const val START_Y = 700f
const val STEP_X = 60f

@Composable
fun LineChart(
    data: List<WeatherItem> = emptyList()
) {

    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    var zoomCentroid by remember { mutableStateOf(Offset(0f, 0f)) }

    Canvas(modifier = Modifier
        .pointerInput(Unit) {
            detectTransformGestures { centroid, pan, zoom, _ ->
                scale *= zoom
                zoomCentroid = centroid
                offset = Offset(offset.x + pan.x, offset.y + pan.y)
            }
        }
        .fillMaxSize()
        .background(Color.DarkGray)
    ) {
        if (data.isNotEmpty()) {
            withTransform({
                scale(scale, scale, zoomCentroid)
                translate(offset.x, offset.y)
            }) {
                drawAxis(data)
                drawLabels(data)
                drawWeatherPath(data)
            }
        } else {
            val textPaint = Paint().apply {
                color = 0xFFB2B2B2.toInt()
                textSize = 16.sp.toPx()
                isAntiAlias = true
            }

            drawContext.canvas.nativeCanvas.drawText(
                "Empty Data!",
                size.width / 2,
                size.height / 2,
                textPaint
            )
        }
    }
}

fun DrawScope.drawAxis(
    data: List<WeatherItem> = emptyList()
) {
    
    drawLine(
        color = Color(0xFFB2B2B2),
        start = Offset(START_X, START_Y),
        end = Offset(START_X + data.size * STEP_X, START_Y),
        strokeWidth = 1.dp.toPx()
    )
    drawLine(
        color = Color(0xFFB2B2B2),
        start = Offset(START_X, START_Y),
        end = Offset(START_X, START_Y - 600f),
        strokeWidth = 1.dp.toPx()
    )
}

fun DrawScope.drawLabels(
    data: List<WeatherItem> = emptyList()
) {
    val xLabelPaint: Paint = Paint().apply {
        color = android.graphics.Color.BLUE
        textSize = 12.sp.toPx()
        isAntiAlias = true
    }

    val yLabelPaint: Paint = Paint().apply {
        color = android.graphics.Color.RED
        textSize = 12.sp.toPx()
        isAntiAlias = true
    }

    data.forEachIndexed { pos, weatherItem ->
        drawContext.canvas.nativeCanvas.drawText(
            TimeUtil.formatHour(weatherItem.hour),
            START_X + pos * STEP_X,
            START_Y + 50,
            xLabelPaint
        )
    }

    repeat(4) { index ->
        drawContext.canvas.nativeCanvas.drawText(
            (index * 10).toString(),
            START_X - STEP_X,
            START_Y - index * 200,
            yLabelPaint
        )
    }
}

fun DrawScope.drawWeatherPath(
    data: List<WeatherItem> = emptyList()
) {
    if (data.isNotEmpty()) {
        val path = Path().apply {
            moveTo(START_X, START_Y - 200 - data.first().temp * 20)
            data.forEachIndexed { pos, weatherItem ->
                if (pos > 0) {
                    lineTo(START_X + pos * STEP_X, START_Y - 200 - weatherItem.temp * 20)
                }
            }
        }
        drawPath(
            path = path,
            color = Color.Magenta,
            style = Stroke(width = 1.dp.toPx())
        )
    }
}

@Composable
@Preview
fun PreviewLineChart() {
    LineChart()
}

@Composable
@Preview
fun PreviewLineChartWithData() {
    LineChart(data = listOf(
        WeatherItem("2025-02-18T08:00", 11f),
        WeatherItem("2025-02-18T09:00", 12f),
        WeatherItem("2025-02-18T10:00", 13f),
        WeatherItem("2025-02-18T11:00", 11f),
        WeatherItem("2025-02-18T12:00", 15f),
    ))
}

