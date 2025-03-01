package com.johny.weatherc.presentation.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johny.weatherc.domain.model.WeatherItem

@Composable
fun TableChart(
    data: List<WeatherItem> = emptyList()
) {
    val highLightBg = Color(128, 236,204,156)
    val normalBg = Color(24, 0, 0, 0)
    val navigationBarHeight = WindowInsets.systemBars.getBottom(LocalDensity.current)
    val navigationBarHeightDp = with(LocalDensity.current) { navigationBarHeight.toDp() }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.DarkGray),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Hour",
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .width(128.dp)
            )
            Spacer(modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(Color.Gray))
            Text(
                "Temperature (°C)",
                color = Color.LightGray,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(12.dp)
                    .weight(1f)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            itemsIndexed(data) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(color = if (index % 2 == 0)  highLightBg else normalBg),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.hour,
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .width(128.dp)
                    )
                    Spacer(modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .background(Color.Gray))
                    Text(
                        "${item.temp} ${item.tempUnit}",
                        color = Color.LightGray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(1f)
                    )
                }
            }
            item {
                Spacer(modifier = Modifier.height(navigationBarHeightDp))
            }
        }
    }
}
