package com.example.webserviceexemple.domain.auth

data class AuthRequest(
    val requestId: String,
    val user: String,
    val action: String,
    val message: String
)

enum class AuthStatus {
    SUCCESS,
    CANCELLED
}

data class AuthResult(
    val requestId: String,
    val status: AuthStatus
)
