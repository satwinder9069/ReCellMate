package com.example.ecopulse.ui.screens

import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecopulse.routes.Screen
import com.example.ecopulse.utils.InfoFormViewModel
import com.example.ecopulse.utils.getFileFromUri
import com.example.ecopulse.utils.imageFileToBase64

@Composable
fun ReviewScreen(
    imageUri: String?,
    navController: NavController,
    viewModel: InfoFormViewModel
) {
    val formState = viewModel.formState
    val context = LocalContext.current
    val file = getFileFromUri(context, imageUri)
    Log.d("FILE", "$file")
    if (file == null) {
        Toast.makeText(context, "Image not found!", Toast.LENGTH_SHORT).show()
    }
    val uri = Uri.parse(imageUri)
    file?.let {
        viewModel.updateCapturedImageFile(it)
    }
    val base64String = imageFileToBase64(file!!)
    if (base64String.isNullOrEmpty()) {
        Log.d("Base64", "Base64 conversion failed or result is empty")
    } else {
        Log.d("Base64", "Base64 conversion successful: ${base64String.take(100)}...")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        file.let {
            Image(
                painter = rememberAsyncImagePainter(model = it),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = {
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(Color(155, 168, 171 )),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Retake Picture", color = Color(6, 20, 27 ))
            }

            Button(
                onClick = {
                    if (uri != null) {
                        val encodedUri = Uri.encode(uri.toString())
                        navController.navigate(Screen.InfoFormScreen.createRoute(encodedUri))
                        Log.d("URI REVIEW SCREEN", "$encodedUri")
                    } else {
                        Toast.makeText(context, "No image captured!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(74, 92, 106)),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Use this image", color = Color( 204, 208, 207))
            }
        }
    }
}