package com.example.webserviceexemple.presentation.auth

import com.example.webserviceexemple.domain.auth.AuthRequest

data class WebAuthUiState(
    val pendingRequest: AuthRequest? = null
)

sealed interface WebAuthIntent {
    data class BridgeRequested(val request: AuthRequest) : WebAuthIntent
    data class UserDecision(val approved: Boolean) : WebAuthIntent
    data object Dismiss : WebAuthIntent
}

sealed interface WebAuthEffect {
    data class EvaluateJavascript(val script: String) : WebAuthEffect
}
