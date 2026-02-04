package com.example.webserviceexemple.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class WebAuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WebAuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<WebAuthEffect>(extraBufferCapacity = 1)
    val effects = _effects

    fun onIntent(intent: WebAuthIntent) {
        when (intent) {
            is WebAuthIntent.BridgeRequested -> {
                _uiState.value = _uiState.value.copy(pendingRequest = intent.request)
            }

            is WebAuthIntent.UserDecision -> {
                val request = _uiState.value.pendingRequest ?: return
                val responseJson = JSONObject().apply {
                    put("requestId", request.requestId)
                    put("status", if (intent.approved) "SUCCESS" else "CANCELLED")
                }.toString()

                val script = "window.onAuthResult(${JSONObject.quote(responseJson)})"

                viewModelScope.launch {
                    _effects.emit(WebAuthEffect.EvaluateJavascript(script))
                }

                _uiState.value = _uiState.value.copy(pendingRequest = null)
            }

            WebAuthIntent.Dismiss -> {
                _uiState.value = _uiState.value.copy(pendingRequest = null)
            }
        }
    }
}
