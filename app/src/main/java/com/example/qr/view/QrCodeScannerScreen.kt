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
@Composable
fun QrCodeScannerScreen(onQrCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    
    var flashEnabled by remember { mutableStateOf(false) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Camera Preview
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    previewView = this
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Top Control Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(
                    color = Color(0xFF424242),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gallery Button
            IconButton(onClick = {
                // TODO: Implement gallery picker
            }) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Gallery",
                    tint = Color.White
                )
            }
            
            // Flash Button
            IconButton(onClick = {
                flashEnabled = !flashEnabled
                // Rebind camera use case with new flash setting
                bindCameraUseCases(
                    context,
                    lifecycleOwner,
                    cameraProviderFuture,
                    previewView,
                    cameraSelector,
                    flashEnabled,
                    executor,
                    onQrCodeScanned
                )
            }) {
                Icon(
                    imageVector = if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Flash",
                    tint = Color.White
                )
            }
            
            // Camera Flip Button
            IconButton(onClick = {
                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
                // Rebind camera use case with new camera selector
                bindCameraUseCases(
                    context,
                    lifecycleOwner,
                    cameraProviderFuture,
                    previewView,
                    cameraSelector,
                    flashEnabled,
                    executor,
                    onQrCodeScanned
                )
            }) {
                Icon(
                    imageVector = Icons.Default.FlipCamera,
                    contentDescription = "Flip Camera",
                    tint = Color.White
                )
            }
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            executor.shutdown()
        }
    }
}

private fun bindCameraUseCases(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    previewView: PreviewView?,
    cameraSelector: CameraSelector,
    flashEnabled: Boolean,
    executor: ExecutorService,
    onQrCodeScanned: (String) -> Unit
) {
    try {
        val cameraProvider = cameraProviderFuture.get()
        
        val preview = Preview.Builder()
            .build()
            .apply {
                surfaceProvider = previewView?.surfaceProvider
            }
        
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .apply {
                setAnalyzer(executor, QrCodeAnalyzer { result ->
                    onQrCodeScanned(result)
                })
            }
        
        // Set flash mode
        val camera = cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageAnalysis
        )
        
        camera.cameraControl.enableTorch(flashEnabled)
        
    } catch (e: Exception) {
        e.printStackTrace()
    }
}