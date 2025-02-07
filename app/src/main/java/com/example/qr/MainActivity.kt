package com.example.qr

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.qr.ui.theme.QrTheme
import com.example.qr.view.QrCodeScannerScreen
import com.example.qr.view.openUrlInBrowser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            QrCodeScannerScreen(onQrCodeScanned = { qrCode ->
                if (qrCode.startsWith("http")) { // Sadece URL'leri kontrol eder.
                    openUrlInBrowser(context, qrCode)
                } else {
                    // QR kodunda URL yoksa yapılacak işlem.
                    Log.d("QrCodeScanner", "QR code is not a URL: $qrCode")
                }
            })
        }
    }
}
