package com.example.qr.view

import androidx.camera.core.Camera
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.fontscaling.MathUtils.lerp
import androidx.compose.ui.unit.sp


@Composable
fun CameraZoomControl(
    camera: Camera?,
    modifier: Modifier = Modifier
) {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val cameraInfo = camera?.cameraInfo
    val zoomRatio = cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
    val maxZoom = cameraInfo?.zoomState?.value?.maxZoomRatio ?: 1f
    val minZoom = cameraInfo?.zoomState?.value?.minZoomRatio ?: 1f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "-",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
                camera?.cameraControl?.setZoomRatio(
                    lerp(minZoom, maxZoom, newValue)
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White.copy(alpha = 0.5f)
            )
        )

        Text(
            text = "+",
            color = Color.White,
            fontSize = 20. sp,
            fontWeight = FontWeight.Bold
        )
    }
}