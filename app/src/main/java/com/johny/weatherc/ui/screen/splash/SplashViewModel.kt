package com.johny.weatherc.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {

    private val _uiEvent = MutableStateFlow("")
    val uiEvent: StateFlow<String> get() = _uiEvent

    init {
        viewModelScope.launch {
            delay(2000)
            _uiEvent.value = "main"
        }
    }
}