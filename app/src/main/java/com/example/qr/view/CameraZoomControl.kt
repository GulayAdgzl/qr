package com.example.qr.view

import androidx.camera.core.Camera
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraZoomControl(
    camera: Camera?,
    modifier: Modifier = Modifier
) {
    var zoomRatio by remember { mutableStateOf(0f) }

    LaunchedEffect(camera) {
        camera?.cameraInfo?.zoomState?.value?.let { zoomState ->
            zoomRatio = (zoomState.zoomRatio - zoomState.minZoomRatio) /
                    (zoomState.maxZoomRatio - zoomState.minZoomRatio)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Remove,
            contentDescription = "Zoom Out",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )

        Slider(
            value = zoomRatio,
            onValueChange = { ratio ->
                zoomRatio = ratio
                camera?.cameraInfo?.zoomState?.value?.let { zoomState ->
                    val zoom = zoomState.minZoomRatio + (zoomState.maxZoomRatio - zoomState.minZoomRatio) * ratio
                    camera.cameraControl.setZoomRatio(zoom)
                }
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

        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Zoom In",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }
}
