package com.example.qr

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.example.qr.view.QrCodeScannerScreen
import com.example.qr.view.openUrlInBrowser

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide system UI
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Use a no AppBar theme
            MaterialTheme(
                colorScheme = dynamicDarkColorScheme(LocalContext.current) // or your preferred color scheme
            ) {
                val context = LocalContext.current

                // Add system UI handling

                    QrCodeScannerScreen(onQrCodeScanned = { qrCode ->
                        if (qrCode.startsWith("http")) {
                            openUrlInBrowser(context, qrCode)
                        } else {
                            Log.d("QrCodeScanner", "QR code is not a URL: $qrCode")
                        }
                    })

            }
        }
    }
}