package com.johny.weatherc.presentation.ui.screen.main

sealed class MainUiEvent {

    data object Reload: MainUiEvent()
    data class ShowTip(
        val message: String,
        val autoHide: Boolean = false
    ): MainUiEvent()

}