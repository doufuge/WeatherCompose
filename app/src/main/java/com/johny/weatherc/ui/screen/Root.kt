package com.johny.weatherc.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.johny.weatherc.ui.screen.main.MainScreen
import com.johny.weatherc.ui.screen.splash.SplashScreen

@Composable
fun Root(
    rootState: RootState = rememberRootState(),
    onMainBack: () -> Unit
) {
    if (rootState.isOnline) {
        NavHost(
            navController = rootState.navController,
            startDestination = Routes.Splash.route
        ) {
            composable(Routes.Splash.route) {
                SplashScreen(
                    navToMain = { rootState.navigateToMain() }
                )
            }
            composable(Routes.Main.route) {
                MainScreen()
                BackHandler {
                    onMainBack()
                }
            }
        }
    } else {
        Text("还没有可用的网络")
    }
}