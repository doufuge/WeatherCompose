package com.johny.weatherc.ui.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberRootState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState:SnackbarHostState = remember { SnackbarHostState() },
    context: Context = LocalContext.current
) = remember(navController, context) {
    RootState(navController, snackbarHostState, context)
}

class RootState(
    val navController: NavHostController,
    private val snackbarHostState: SnackbarHostState,
    private val context: Context
) {

    var isOnline by mutableStateOf(checkIfOnline())
        private set

    fun refreshOnline() {
        isOnline = checkIfOnline()
    }

    suspend fun showSnackbar(msg: String, duration: SnackbarDuration) {
        snackbarHostState.showSnackbar(message = msg, duration = duration)
    }

    fun navigateToMain() {
        navController.navigate(Routes.Main.route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    private fun checkIfOnline(): Boolean {
        val cm = ContextCompat.getSystemService(context, ConnectivityManager::class.java)
        cm?.getNetworkCapabilities(cm.activeNetwork)?.let { capabilities ->
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        }
        return false
    }

}