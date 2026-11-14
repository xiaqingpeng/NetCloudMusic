package com.example.netcloudmusic.feature.scan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kashif.cameraK.compose.CameraPreviewView
import com.kashif.cameraK.compose.rememberCameraKState
import com.kashif.cameraK.enums.CameraLens
import com.kashif.cameraK.enums.FlashMode
import com.kashif.cameraK.permissions.providePermissions
import com.kashif.cameraK.state.CameraKState
import com.kashif.cameraK.state.CameraConfiguration
import com.kashif.imagesaverplugin.ImageSaverConfig
import com.kashif.imagesaverplugin.rememberImageSaverPlugin
import com.kashif.qrscannerplugin.rememberQRScannerPlugin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ScanRoute(
    toBack: () -> Unit,
    onScanResult: (String) -> Unit,
) {
    val permissions = providePermissions()
    var cameraGranted by remember { mutableStateOf(permissions.hasCameraPermission()) }
    if (!cameraGranted) {
        permissions.RequestCameraPermission(
            onGranted = { cameraGranted = true },
            onDenied = { cameraGranted = false },
        )
    }

    if (cameraGranted) {
        ScanScreen(toBack = toBack, onScanResult = onScanResult)
    } else {
        PermissionDeniedScreen(toBack = toBack)
    }
}

@Composable
private fun PermissionDeniedScreen(toBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "需要相机权限才能扫码",
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = toBack) { Text("返回") }
        }
    }
}

@Composable
private fun ScanScreen(
    toBack: () -> Unit,
    onScanResult: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val qrScannerPlugin = rememberQRScannerPlugin(scope)
    val imageSaverPlugin = rememberImageSaverPlugin(
        config = ImageSaverConfig(
            isAutoSave = true,
            prefix = "NetCloudMusic",
        ),
    )
    val cameraState by rememberCameraKState(
        config = CameraConfiguration(
            cameraLens = CameraLens.BACK,
            flashMode = FlashMode.OFF,
        ),
        setupPlugins = { stateHolder ->
            stateHolder.attachPlugin(qrScannerPlugin)
            stateHolder.attachPlugin(imageSaverPlugin)
        },
    )
    var handledCode by remember { mutableStateOf<String?>(null) }
    var zoom by remember { mutableStateOf(1f) }
    var flashEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(qrScannerPlugin) {
        qrScannerPlugin.getQrCodeFlow().collect { value ->
            if (value != handledCode) {
                handledCode = value
                qrScannerPlugin.pauseScanning()
                onScanResult(value)
                delay(1200)
                handledCode = null
                qrScannerPlugin.resumeScanning()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        when (val state = cameraState) {
            CameraKState.Initializing -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
            )
            is CameraKState.Error -> Text(
                text = state.message,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center).padding(24.dp),
            )
            is CameraKState.Ready -> {
                CameraPreviewView(
                    controller = state.controller,
                    modifier = Modifier.fillMaxSize(),
                )
                ScanOverlay()
                IconButton(
                    onClick = toBack,
                    modifier = Modifier.align(Alignment.TopStart).padding(top = 8.dp),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "返回", tint = Color.White)
                }
                Row(
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 8.dp, end = 8.dp),
                ) {
                    IconButton(onClick = {
                        state.controller.toggleCameraLens()
                    }) {
                        Icon(Icons.Default.SwapHoriz, "切换镜头", tint = Color.White)
                    }
                    IconButton(onClick = {
                        state.controller.toggleFlashMode()
                        flashEnabled = !flashEnabled
                    }) {
                        Icon(
                            if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            "闪光灯",
                            tint = Color.White,
                        )
                    }
                }
                Column(
                    modifier = Modifier.align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("1x", color = Color.White)
                        Slider(
                            value = zoom,
                            onValueChange = {
                                zoom = it
                                state.controller.setZoom(it)
                            },
                            valueRange = 1f..state.uiState.maxZoom.coerceAtLeast(1f),
                            modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                        )
                        Text("${state.uiState.maxZoom.toInt()}x", color = Color.White)
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                state.controller.takePictureToFile()
                                snackbarHostState.showSnackbar("照片已保存")
                            }
                        }) {
                            Icon(Icons.Default.PhotoLibrary, "拍照并保存", tint = Color.White, modifier = Modifier.size(34.dp))
                        }
                        IconButton(onClick = {
                            scope.launch {
                                state.controller.takePictureToFile()
                                snackbarHostState.showSnackbar("照片已保存")
                            }
                        }) {
                            Icon(Icons.Default.CameraAlt, "拍照", tint = Color.White, modifier = Modifier.size(56.dp))
                        }
                        IconButton(onClick = { qrScannerPlugin.resumeScanning() }) {
                            Icon(Icons.Default.QrCodeScanner, "继续扫码", tint = Color.White, modifier = Modifier.size(34.dp))
                        }
                    }
                }
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 150.dp),
        )
    }
}

@Composable
private fun ScanOverlay() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.size(260.dp),
        ) {
            androidx.compose.foundation.Canvas(Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.White.copy(alpha = 0.9f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx()),
                )
            }
            Text(
                text = "将二维码放入框内",
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 18.dp),
            )
        }
    }
}
