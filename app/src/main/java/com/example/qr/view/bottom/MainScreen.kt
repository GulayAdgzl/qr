package com.example.qr.view.bottom

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.qr.R
import com.example.qr.view.openUrlInBrowser
import com.rahad.riobottomnavigation.composables.RioBottomNavItemData
import com.rahad.riobottomnavigation.composables.RioBottomNavigation
/*
@Composable
fun MainScreen() {
    // Constants for icon resources and labels
    val items = listOf(
        R.drawable.qr,
        R.drawable.history,

    )
    val labels = listOf(
        "Generate",
        "History",
    )

    val selectedIndex = rememberSaveable { mutableIntStateOf(0) }
    var showQRScanner by remember { mutableStateOf(false) }

    val buttons = items.mapIndexed { index, iconData ->
        RioBottomNavItemData(
            imageVector = ImageVector.vectorResource(iconData),
            selected = index == selectedIndex.intValue && !showQRScanner,
            onClick = {
                selectedIndex.intValue = index
                showQRScanner = false
            },
            label = labels[index]
        )
    }

    Scaffold(
        bottomBar = {
            val fabColor:Color= Color
            RioBottomNavigation(
                fabIcon = ImageVector.vectorResource(id = R.drawable.bottomqr),
                buttons = buttons,
                fabSize = 70.dp,
                barHeight = 70.dp,
                selectedItemColor = fabColor,
                fabBackgroundColor = fabColor,
                onFabClick = { showQRScanner = true }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            if (showQRScanner) {
                QrCodeScannerScreen(onQrCodeScanned = { qrCode ->
                    val context = LocalContext.current
                    if (qrCode.startsWith("http")) {
                        openUrlInBrowser(context, qrCode)
                    } else {
                        Toast.makeText(
                            context,
                            "QR Code içeriği: $qrCode",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                ScreenContent(selectedIndex.intValue)
            }
        }
    }
}*/