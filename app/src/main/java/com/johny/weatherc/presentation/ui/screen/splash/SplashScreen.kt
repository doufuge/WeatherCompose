package com.johny.weatherc.presentation.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.johny.weatherc.R
import com.johny.weatherc.presentation.ui.theme.WeatherCTheme

@Composable
fun SplashScreen(
    navToMain: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {

    val uiEvent by viewModel.uiEvent.collectAsStateWithLifecycle()
    val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.fishing))

    LaunchedEffect(uiEvent) {
        if (uiEvent == "main") {
            navToMain()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = colorResource(R.color.bg_splash))
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Image(
            painter = painterResource(R.drawable.ic_fishing),
            contentDescription = stringResource(id = R.string.app_name),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(128.dp),
            colorFilter = ColorFilter.tint(colorResource(R.color.primary))
        )
        Spacer(modifier = Modifier.height(12.dp))
        LottieAnimation(
            composition = lottie,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.fillMaxWidth().wrapContentHeight().offset(y = 72.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    WeatherCTheme {
        SplashScreen(navToMain = {})
    }
}