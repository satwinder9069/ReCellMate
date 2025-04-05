package com.example.ecopulse.utils

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

@Composable
fun RequestPermissions(
    onAllPermissionsGranted: () -> Unit
) {
    val context = LocalContext.current
    val permissions = arrayOf(
        Manifest.permission.READ_MEDIA_IMAGES
    )
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areAllPermissionsGranted = permissionsMap.values.all { it }
        if (areAllPermissionsGranted) {
            onAllPermissionsGranted()
        }
    }

    LaunchedEffect(Unit) {
        val areAllPermissionsAlreadyGranted = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PermissionChecker.PERMISSION_GRANTED
        }
        if (areAllPermissionsAlreadyGranted) {
            onAllPermissionsGranted()
        } else {
            permissionLauncher.launch(permissions)
        }
    }
}