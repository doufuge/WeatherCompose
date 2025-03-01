package com.johny.weatherc.presentation.ui.screen

sealed class Routes(val route: String) {

//    data object Root: Routes("root")

    data object Splash: Routes("splash")

    data object Main: Routes("main")

}