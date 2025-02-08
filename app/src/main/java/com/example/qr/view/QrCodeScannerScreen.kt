package com.example.qr.view

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.common.HybridBinarizer
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@Composable
fun QrCodeScannerScreen(onQrCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor: ExecutorService = Executors.newSingleThreadExecutor()
    var camera by remember { mutableStateOf<Camera?>(null) }
    var flashEnabled by remember { mutableStateOf(false) }
    var cameraSelector by remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    var previewView: PreviewView? by remember { mutableStateOf(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedImageUri ->
            try {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val source = ImageDecoder.createSource(context.contentResolver, selectedImageUri)
                    ImageDecoder.decodeBitmap(source)
                } else {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, selectedImageUri)
                }

                val intArray = IntArray(bitmap.width * bitmap.height)
                bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
                val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
                val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

                try {
                    val result = MultiFormatReader().decode(binaryBitmap)
                    onQrCodeScanned(result.text)
                } catch (e: Exception) {
                    Toast.makeText(context, "QR kod bulunamadı!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Resim işlenirken hata oluştu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
            IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Gallery",
                    tint = Color.White
                )
            }

            IconButton(onClick = {
                flashEnabled = !flashEnabled
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

            IconButton(onClick = {
                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
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
                    imageVector = Icons.Default.FlipCameraAndroid,
                    contentDescription = "Flip Camera",
                    tint = Color.White
                )
            }
        }



        // Bottom Navigation Bar



        // Zoom Control
        CameraZoomControl(
            camera = camera,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp)
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            executor.shutdown()
        }
    }

    LaunchedEffect(cameraProviderFuture, previewView) {
        try {
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
        } catch (e: Exception) {
            e.printStackTrace()
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
        cameraProvider.unbindAll()

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

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also { get(it) }
    }

