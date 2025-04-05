package com.example.ecopulse.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.ecopulse.routes.Screen
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLDecoder

fun captureImage(
    imageCapture: ImageCapture?,
    context: Context,
    viewModel: InfoFormViewModel,
    navController: NavController,
    onImageCaptured: (String) -> Unit

) {
    if (imageCapture == null) {
        Toast.makeText(context, "Camera not ready", Toast.LENGTH_SHORT).show()
        return
    }

    val photoFile = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "${System.currentTimeMillis()}.jpg")
    if (photoFile.parentFile?.exists() != true) {
        photoFile.parentFile?.mkdirs()
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
    try {
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val imageUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.provider",
                        photoFile
                    )
                    viewModel.updateCapturedImageFile(photoFile)
                    Log.d("CAPTURE FUNCTION", "Captured Image File Updated: ${photoFile.absolutePath}")
                    Toast.makeText(context, "Image File Updated!", Toast.LENGTH_SHORT).show()

                    onImageCaptured(photoFile.absolutePath)

                    val encodedUri = Uri.encode(imageUri.toString())
                    navController.navigate(Screen.ReviewScreen.createRoute(encodedUri))

                    Toast.makeText(context, "Image Captured!", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: ImageCaptureException) {
                    Toast.makeText(
                        context,
                        "Failed to capture image: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    } catch (e: Exception) {
        Toast.makeText(context, "Error capturing image: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}



fun saveImageToGallery(context: Context, photoFile: File): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/EcoPulse")
    }

    val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
        context.contentResolver.openOutputStream(it)?.use { outputStream ->
            if (photoFile.exists()) {
                photoFile.inputStream().copyTo(outputStream)
                Log.d("SaveImage", "${photoFile .absolutePath}")
            } else {
                Log.e("SaveImage", "File not found: ${photoFile.absolutePath}")
                return null
            }
        }
    }

    return uri
}

fun imageFileToBase64(file: File): String? {
    return try {
        if (!file.exists()) {
            Log.d("Conversion Base64", "File does not exist: ${file.absolutePath}")
            return null
        }
        val bytes = file.readBytes()
        Log.d("Base64", "File size: ${bytes.size} bytes")
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getFileFromUri(context: Context, imageUri: String?): File? {
    Log.d("MultipartDebug", "getFileFromUri called with imageUri: $imageUri")

    if (imageUri.isNullOrEmpty()) {
        Log.d("MultipartDebug", "imageUri is null or empty")
        return null
    }

    val decodedUriStr = URLDecoder.decode(imageUri, "UTF-8")
    val uri = Uri.parse(decodedUriStr)
    Log.d("MultipartDebug", "Decoded URI: $decodedUriStr")
    Log.d("MultipartDebug", "Parsed URI: $uri")

    return when {
        decodedUriStr.startsWith("/storage") || decodedUriStr.startsWith("file://") || decodedUriStr.startsWith("/data") -> {
            val file = File(uri.path ?: return null)
            Log.d("MultipartDebug", "Returning direct File: ${file.absolutePath}")
            file
        }

        decodedUriStr.startsWith("content://") -> {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: run {
                    Log.d("MultipartDebug", "Failed to open inputStream for content URI")
                    return null
                }
                val tempFile = File(context.cacheDir, "uploaded_image_${System.currentTimeMillis()}.jpg")
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                    Log.d("MultipartDebug", "File copied to tempFile: ${tempFile.absolutePath}")
                }
                tempFile
            } catch (e: Exception) {
                Log.d("MultipartDebug", "Exception in content URI handling: ${e.message}")
                e.printStackTrace()
                null
            }
        }

        else -> {
            Log.d("MultipartDebug", "Unknown URI scheme after decoding")
            null
        }
    }
}



fun imageProxyToBitmap(image: ImageProxy): Bitmap {
    val buffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun saveImageToInternalStorage(context: Context, bitmap: Bitmap, fileName: String): Uri {
    val file = File(context.filesDir, "$fileName.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return Uri.fromFile(file)
}