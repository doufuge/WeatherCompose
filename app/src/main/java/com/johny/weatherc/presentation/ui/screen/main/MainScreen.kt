package com.johny.weatherc.presentation.ui.screen.main

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.johny.weatherc.R
import com.johny.weatherc.presentation.ui.theme.WeatherCTheme
import kotlinx.coroutines.delay

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showTip by remember { mutableStateOf(false) }
    var tip by remember { mutableStateOf("") }

    val locationPermissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        if (permissions.all { it.value }) {
            viewModel.getLocation()
        } else {
            tip = ContextCompat.getString(context, R.string.no_permission_tip)
            showTip = true
        }
    }

    LaunchedEffect(state.uiEvent) {
        state.uiEvent.let { uiEvent ->
            when (uiEvent) {
                is MainUiEvent.ShowTip -> {
                    tip = uiEvent.message
                    showTip = true
                    if (uiEvent.autoHide) {
                        delay(3000)
                        showTip = false
                    }
                }

                is MainUiEvent.Reload -> {

                }

                null -> {}
            }
        }
    }

    LaunchedEffect(Unit) {
        locationPermissionState.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ActionBar(
                showTable = state.showTable,
                showAction = !state.loading
            ) { tag ->
                if (tag == "toggleViewType") {
                    viewModel.toggleViewType()
                } else if (tag == "reload") {
                    viewModel.fetchWeather()
                }
            }

            AnimatedContent(
                targetState = state.showTable,
                transitionSpec = { getChartTransition(state.showTable) }
            ) { target ->
                if (target) {
                    TableChart(data = state.data)
                } else {
                    LineChart(data = state.data)
                }
            }
        }

        TipBar(show = showTip, tip = tip, modifier = Modifier.align(Alignment.TopCenter))
        LottieHud(show = state.loading, modifier = Modifier.align(Alignment.Center))
    }

}

@Composable
fun TipBar(show: Boolean, tip: String, modifier: Modifier) {
    val statusBarHeight = WindowInsets.systemBars.getTop(LocalDensity.current)
    val statusBarHeightDp = with(LocalDensity.current) { statusBarHeight.toDp() }
    AnimatedVisibility(
        visible = show,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Yellow, shape = RoundedCornerShape(12.dp))
        ) {
            Spacer(modifier = Modifier.height(statusBarHeightDp + 12.dp))
            Text(
                text = tip,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun LottieHud(show: Boolean, modifier: Modifier) {
    val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.hud))
    if (show) {
        LottieAnimation(
            composition = lottie,
            iterations = LottieConstants.IterateForever,
            modifier = modifier
                .size(108.dp)
                .background(Color(0xFF9A9A9A), shape = RoundedCornerShape(20.dp))
                .padding(20.dp)
        )
    }
}

fun getChartTransition(targetState: Boolean): ContentTransform {
    val x = if (targetState) 200 else -200
    return slideInHorizontally(animationSpec = tween(durationMillis = 200)) { x } + slideInHorizontally(
        animationSpec = tween(delayMillis = 200, durationMillis = 200)
    ) { 0 } + scaleIn(
        animationSpec = tween(durationMillis = 400),
        initialScale = 0.7f
    ) togetherWith
            slideOutHorizontally(animationSpec = tween(durationMillis = 200)) { -x } + slideOutHorizontally(
        animationSpec = tween(delayMillis = 200, durationMillis = 200)
    ) { x } + scaleOut(
        animationSpec = tween(durationMillis = 400),
        targetScale = 0.7f
    )
}

@Preview
@Composable
fun PreviewLottieHud() {
    WeatherCTheme {
        LottieHud(true, Modifier)
    }
}

@Preview
@Composable
fun PreviewTipBar() {
    WeatherCTheme {
        TipBar(true, "Tip tip tip", Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    WeatherCTheme {
        MainScreen(viewModel = hiltViewModel())
    }
}
