package com.example.qr.view

import android.graphics.ImageFormat
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.viewinterop.AndroidView

import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@OptIn(ExperimentalGetImage::class)
@Composable
fun QrCodeScannerScreen(onQrCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor: ExecutorService = Executors.newSingleThreadExecutor()

    var previewView: PreviewView? by remember { mutableStateOf(null) }

    // Kamera önizlemesini ekliyoruz
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                previewView = this
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        modifier = Modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().apply {
            surfaceProvider=previewView?.surfaceProvider
        }


        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        val analyzer = ZXingAnalyzer()

        // QR kodu analiz etmek için bir analizör ekliyoruz
        imageAnalysis.setAnalyzer(executor) { imageProxy ->
            val image = imageProxy.image
            if (image != null && image.format == ImageFormat.YUV_420_888) {
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val result = analyzer.analyze(image, rotationDegrees)
                result?.let { qrCode ->
                    onQrCodeScanned(qrCode)
                }
            }
            imageProxy.close()
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            cameraProvider.unbindAll()
            executor.shutdown()
        }
    }
}