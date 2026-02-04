package com.example.webserviceexemple.presentation.webview

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.JavascriptInterface
import com.example.webserviceexemple.domain.auth.AuthRequest
import org.json.JSONObject

class AndroidAuthBridge(
    private val onRequest: (AuthRequest) -> Unit
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    @JavascriptInterface
    fun request(payload: String) {
        try {
            val json = JSONObject(payload)
            val request = AuthRequest(
                requestId = json.getString("requestId"),
                user = json.getString("user"),
                action = json.getString("action"),
                message = json.getString("message")
            )

            mainHandler.post {
                onRequest(request)
            }
        } catch (e: Exception) {
            Log.e("AndroidAuthBridge", "Invalid payload", e)
        }
    }
}
