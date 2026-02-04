package com.example.webserviceexemple.presentation.auth

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.webserviceexemple.presentation.webview.AndroidAuthBridge
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebAuthScreen(viewModel: WebAuthViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    val currentWebView by rememberUpdatedState(webViewRef)

    LaunchedEffect(Unit) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                is WebAuthEffect.EvaluateJavascript -> {
                    val wv = currentWebView
                    wv?.post {
                        wv.evaluateJavascript(effect.script, null)
                    }
                }
            }
        }
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            WebView(context).apply {

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                addJavascriptInterface(
                    AndroidAuthBridge { request ->
                        viewModel.onIntent(WebAuthIntent.BridgeRequested(request))
                    },
                    "AndroidAuth"
                )

                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()
                loadUrl("file:///android_asset/web/index.html")
                webViewRef = this
            }
        }
    )

    uiState.pendingRequest?.let { request ->
        AuthDialog(
            request = request,
            onApprove = {
                viewModel.onIntent(WebAuthIntent.UserDecision(approved = true))
            },
            onCancel = {
                viewModel.onIntent(WebAuthIntent.UserDecision(approved = false))
            }
        )
    }
}
