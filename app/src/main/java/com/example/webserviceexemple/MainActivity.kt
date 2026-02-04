package com.example.webserviceexemple

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.webserviceexemple.presentation.auth.WebAuthViewModel
import com.example.webserviceexemple.presentation.auth.WebAuthScreen
import com.example.webserviceexemple.ui.theme.WebServiceExempleTheme

class MainActivity : ComponentActivity() {
    private val webAuthViewModel: WebAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebServiceExempleTheme {
                WebAuthScreen(webAuthViewModel)
            }
        }
    }
}
