package com.johny.weatherc.ui.screen.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.johny.weatherc.R
import com.johny.weatherc.ui.theme.WeatherCTheme

@Composable
fun ActionBar(
    showTable: Boolean,
    showAction: Boolean,
    onActionClick: (tag: String) -> Unit,
) {
    val statusBarHeight = WindowInsets.systemBars.getTop(LocalDensity.current)
    val statusBarHeightDp = with(LocalDensity.current) { statusBarHeight.toDp() }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarHeightDp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Weather",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
            if (showAction) {
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    ImageActionButton(
                        modifier = Modifier.clickable {
                            onActionClick("reload")
                        },
                        painter = painterResource(R.drawable.ic_reload)
                    )
                    AnimatedContent(
                        targetState = showTable,
                        transitionSpec = {
                            fadeIn(
                                animationSpec = tween(400)
                            ) togetherWith fadeOut(
                                animationSpec = tween(400)
                            )
                        }
                    ) { target ->
                        ImageActionButton(
                            modifier = Modifier.clickable {
                                onActionClick("toggleViewType")
                            },
                            painter = painterResource(if (target) R.drawable.ic_line_chart else R.drawable.ic_list_chart)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ImageActionButton(
    modifier: Modifier = Modifier,
    painter: Painter,
    contentDescription: String = stringResource(id = R.string.app_name)
) {
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(40.dp)
            .padding(6.dp),
        colorFilter = ColorFilter.tint(colorResource(R.color.primary))
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewActionBar() {
    WeatherCTheme {
        ActionBar(showTable = false, showAction = true) {}
    }
}