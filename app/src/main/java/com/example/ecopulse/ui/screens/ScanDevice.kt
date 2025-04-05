package com.example.ecopulse.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecopulse.R
import com.example.ecopulse.routes.Screen
import com.example.ecopulse.utils.InfoFormViewModel
import com.example.ecopulse.utils.RequestPermissions
import com.example.ecopulse.utils.captureImage
import com.example.ecopulse.utils.getFileFromUri
import com.example.ecopulse.utils.saveImageToGallery
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanDevice(navController: NavController) {

    val context = LocalContext.current
    val viewModel: InfoFormViewModel = viewModel()

    var isFlashOn by remember { mutableStateOf(false) }
    var isBackCamera by remember { mutableStateOf(true) }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    DisposableEffect(Unit) {
        onDispose {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
            Log.d("Camera", "Camera unbound on dispose")
        }
    }
    RequestPermissions(
        onAllPermissionsGranted = {
            Toast.makeText(context, "All permissions granted!", Toast.LENGTH_SHORT).show()
        }
    )
    LaunchedEffect(isBackCamera) {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
        val cameraSelector = if (isBackCamera) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }

        imageCapture = ImageCapture.Builder().build()
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e("Cameras ", "Camera binding failed", e)
        }
    }
    LaunchedEffect(isFlashOn) {
        imageCapture?.flashMode = if (isFlashOn) {
            ImageCapture.FLASH_MODE_ON
        } else {
            ImageCapture.FLASH_MODE_OFF
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("")},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White
                ),
                actions = {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        IconButton(
                            onClick = { isFlashOn = !isFlashOn }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (isFlashOn) R.drawable.is_flash_on else R.drawable.is_flash_off
                                ),
                                contentDescription = "Flash",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        IconButton(
                            onClick = { isBackCamera = !isBackCamera }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.flip_camera),
                                contentDescription = "Camera",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.Black,
                content = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                val isPermissionGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                                } else {
                                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                }
                                if(isPermissionGranted) {

                                    captureImage(
                                        imageCapture, context, viewModel, navController
                                    ) { imageUri ->
                                        Uri.parse(imageUri).path?.let { path ->
                                            val savedUri = saveImageToGallery(context, File(path))
                                            Log.d("SAVED URI", "Saved URI $savedUri")

                                            if (savedUri != null) {
                                                val encodeUri = Uri.encode(savedUri.toString())
                                                navController.navigate(Screen.ReviewScreen.createRoute(encodeUri))
                                                Log.d("ENCODE URI", "$encodeUri")
                                                Toast.makeText(context, "Image Saved in Gallery!", Toast.LENGTH_SHORT).show()
                                            } else {
                                                Toast.makeText(context, "Failed to Save Image!", Toast.LENGTH_SHORT).show()
                                            }
                                        } ?: run {
                                            Toast.makeText(context, "Image Path Error!", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                } else {
                                    Toast.makeText(context, "Please grant permissions to save images", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.size(64.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.is_capturing),
                                contentDescription = "Capture",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }
            )
        }
    ) {padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(
                factory = { previewView },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


