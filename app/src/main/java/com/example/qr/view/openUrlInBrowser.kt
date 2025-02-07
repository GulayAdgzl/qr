package com.example.qr.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast


fun openUrlInBrowser(context: Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("QrCodeScanner", "No browser found: ${e.message}")
        Toast.makeText(context, "Tarayıcı bulunamadı!", Toast.LENGTH_SHORT).show()
    }
}