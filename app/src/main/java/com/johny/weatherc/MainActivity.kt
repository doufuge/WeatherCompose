package com.johny.weatherc

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.johny.weatherc.presentation.ui.screen.Root
import com.johny.weatherc.presentation.ui.screen.Routes
import com.johny.weatherc.presentation.ui.screen.splash.SplashScreen
import com.johny.weatherc.presentation.ui.theme.WeatherCTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                WeatherCTheme {
                    Root {
                        onBackPressedDispatcher.addCallback {
                            moveTaskToBack(true)
                        }
                    }
                }
            }
        }
        fitScreen()
    }

    private fun fitScreen() {
        // 设置背景为透明
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 设置Window的Flags来控制导航栏
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

}