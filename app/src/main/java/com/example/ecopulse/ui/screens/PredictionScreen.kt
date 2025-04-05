package com.example.ecopulse.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecopulse.R
import com.example.ecopulse.data.api.RetrofitInstance
import com.example.ecopulse.data.models.AIInput
import com.example.ecopulse.data.repository.AIRepository
import com.example.ecopulse.routes.Screen
import com.example.ecopulse.ui.viewmodel.AIViewModel
import com.example.ecopulse.ui.viewmodel.AIViewModelFactory
import com.example.ecopulse.utils.InfoFormViewModel
import com.example.ecopulse.utils.imageFileToBase64
import com.example.ecopulse.utils.prepareFilePart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

@Composable
fun PredictionScreen(imageUri: String?, infoFormViewModel: InfoFormViewModel, navController: NavController) {
    val api = RetrofitInstance.api
    val repository = AIRepository(api)
    val aiViewModel: AIViewModel = viewModel(
        factory = AIViewModelFactory(repository)
    )
    val scope = rememberCoroutineScope()
    val result = aiViewModel.predictionResult
    val isLoading = aiViewModel.isLoading
    val imageFile: File? = infoFormViewModel.capturedImageFile
    val context = LocalContext.current


    var predictionAttempted by remember { mutableStateOf(false) }
/*        val imageUri: Uri = Uri.fromFile(imageFile)
        val context = LocalContext.current
        val file = getFileFromContentUri(context, imageUri)
        val file: File? = infoFormViewModel.capturedImageFile.value?.let {
            getFileFromContentUri(
                LocalContext.current,
                Uri.fromFile(it)
            )
        }
    val base64Image = imageFile?.let { imageFileToBase64(it) }
    val base64Url = "data:image/jpeg:base64,$base64Image"
    Log.d("PredictionScreen", "Base64 Image: $base64Image")

        if (imageFile != null) {
            Log.d("PredictionScreen", "Captured Image File Path: ${imageFile.absolutePath}")
            val base64Image = imageFileToBase64(imageFile)
            Log.d("PredictionScreen", "Base64 Image: $base64Image")
        } else {
            Log.d("PredictionScreen", "Captured Image is NULL")
        }
    if (base64Image != null) {
        Log.d("BASE64 IMAGE", "$base64Image")
    }*/

   Scaffold(
       modifier = Modifier.fillMaxSize(),
       bottomBar = {
           NavigationBar(
               modifier = Modifier.fillMaxWidth(),
               containerColor = Color(204, 208, 207 )
           ) {
               Row(
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceEvenly,
                   verticalAlignment = Alignment.CenterVertically
               ) {
                   NavigationBarItem(
                       icon = {
                           Icon(
                               painter = painterResource(R.drawable.home),
                               contentDescription = "Home",
                               tint = Color(6, 20, 27 ),
                               modifier = Modifier.size(35.dp)

                           )
                       },
                       label = { Text(text = "Home") },
                       selected = false,
                       onClick = { navController.navigate(Screen.HomeScreen.route) }
                   )
                   NavigationBarItem(
                       icon = {
                           Icon(
                               painter = painterResource(R.drawable.ai),
                               contentDescription = "Learn more",
                               tint = Color(6, 20, 27 ),
                               modifier = Modifier.size(35.dp)
                           )
                       },
                       label = { Text(text = "Ask to Ai") },
                       selected = false,
                       onClick = { navController.navigate(Screen.LearnMore.route) }
                   )
               }
           }
       }
   ) { padding ->
       Column(
           modifier = Modifier
               .fillMaxSize()
               .background(Color(0xFFF6F9FC))
               .padding(24.dp),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally
       ) {
           Text(
               text = "\uD83D\uDCC8 Price Prediction",
               style = MaterialTheme.typography.headlineMedium,
               color = Color(0xFF34495E)
           )

           Spacer(modifier = Modifier.height(32.dp))

           val imagePartState = remember { mutableStateOf<MultipartBody.Part?>(null) }

           LaunchedEffect(imageUri) {
               imagePartState.value = prepareFilePart("image", imageUri, context)
           }
           Button(
               onClick = {

                   scope.launch {
/*                    try {
                        Log.d("PredictionScreen", "ImageFile: $imageFile")

                        val base64Image = imageFile?.let { imageFileToBase64(it) }
                        Log.d("PredictionScreen", "Base64 Image: $base64Image")*/
                       val aiInput = AIInput(
                           os = infoFormViewModel.formState.os,
                           screenSize = infoFormViewModel.formState.screenSize,
                           fiveG = infoFormViewModel.formState.is5G,
                           internalMemory = infoFormViewModel.formState.internalMemory,
                           ram = infoFormViewModel.formState.ram,
                           battery = infoFormViewModel.formState.batteryInfo,
                           releaseYear = infoFormViewModel.formState.releaseYear,
                           daysUsed = infoFormViewModel.formState.daysUsed,
                           normalizedNewPrice = infoFormViewModel.formState.normalizedNewPrice.toDoubleOrNull()?.div(1000)?.toString() ?: "",
                           deviceBrand = infoFormViewModel.formState.deviceBrand,
                       )

                       if (imagePartState.value != null) {
                           aiViewModel.fetchPrediction(aiInput, imagePartState.value!!)
                       } else {
                           Log.d("PredictionScreen", "Image Part is NULL")
                       }
                   }
               },
               colors = ButtonDefaults.buttonColors(Color(74, 92, 106 )),
               modifier = Modifier
                   .fillMaxWidth()
                   .height(50.dp)
           ) {
               Text("Predict", style = MaterialTheme.typography.bodyLarge, color = Color(204, 208, 207  ))
           }
           Spacer(modifier = Modifier.height(24.dp))


           if (isLoading) {
               Box(
                   modifier = Modifier.fillMaxSize(),
                   contentAlignment = Alignment.Center
               ) {
                   CircularProgressIndicator(
                       color = Color(0xFF3498DB),
                       strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
                   )
               }
           } else {

               if (result != null) {
                   Log.d(
                       "PredictionScreen",
                       "Predicted Price: ${aiViewModel.predictionResult?.predictedPrice}"
                   )
               } else {
                   Log.d("PredictionScreen", "Prediction Result is NULL")

               }
               result?.let { prediction ->
                   AnimatedVisibility(visible = result != null) {
                       Card(
                           modifier = Modifier
                               .fillMaxWidth()
                               .shadow(10.dp, shape = RoundedCornerShape(16.dp)),
                           colors = CardDefaults.cardColors(containerColor = Color.White)
                       ) {
                           Column(
                               modifier = Modifier.padding(20.dp)
                           ) {
                               Text(
                                   text = "\uD83D\uDCCB Condition: ${prediction.condition}",
                                   style = MaterialTheme.typography.titleMedium,
                                   color = Color(0xFF2C3E50)
                               )
                               Spacer(modifier = Modifier.height(8.dp))
                               Text(
                                   text = "✅ Resale Price: ${prediction.finalAdjustedPrice} ₹",
                                   style = MaterialTheme.typography.headlineMedium,
                                   color = Color(6, 20, 27 )
                               )
                               Spacer(modifier = Modifier.height(8.dp))
                               Text(
                                   text = "📈 Predicted Price: ${prediction.predictedPrice}",
                                   style = MaterialTheme.typography.bodyLarge,
                                   color = Color(37, 55, 69)
                               )
                           }
                       }
                   }
               }
               /* if (predictionAttempted && result == null && !isLoading) {
                    TryAgainButton(imageUri, infoFormViewModel, aiViewModel)
                }*/
           }
       }
   }
}

@Composable
private fun TryAgainButton(
    imageUri: String?,
    infoFormViewModel: InfoFormViewModel,
    aiViewModel: AIViewModel
) {
    val context = LocalContext.current
    var shouldRetry by remember { mutableStateOf(false) }
    val imagePartState = remember { mutableStateOf<MultipartBody.Part?>(null) }

    LaunchedEffect(imageUri) {
        imagePartState.value = prepareFilePart("image", imageUri, context)
    }

    LaunchedEffect(shouldRetry) {
        if (shouldRetry) {
            try {
                val aiInput = AIInput(
                    os = infoFormViewModel.formState.os,
                    screenSize = infoFormViewModel.formState.screenSize,
                    fiveG = infoFormViewModel.formState.is5G,
                    internalMemory = infoFormViewModel.formState.internalMemory,
                    ram = infoFormViewModel.formState.ram,
                    battery = infoFormViewModel.formState.batteryInfo,
                    releaseYear = infoFormViewModel.formState.releaseYear,
                    daysUsed = infoFormViewModel.formState.daysUsed,
                    normalizedNewPrice = infoFormViewModel.formState.normalizedNewPrice
                        .toDoubleOrNull()?.div(1000)?.toString() ?: "",
                    deviceBrand = infoFormViewModel.formState.deviceBrand,
                )

                if (imagePartState.value != null) {
                    aiViewModel.fetchPrediction(aiInput, imagePartState.value!!)
                } else {
                    Toast.makeText(context, "Image part is missing!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("TryAgainButton", "Prediction error", e)
            } finally {
                shouldRetry = false
            }
        }
    }

    // Show button only if prediction failed (null) & not loading
    if (aiViewModel.predictionResult == null && !aiViewModel.isLoading) {
        Button(
            onClick = { shouldRetry = true },
            colors = ButtonDefaults.buttonColors(Color(0xFF3498DB)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Try Again", color = Color.White)
        }
    }
}

@Composable
private fun TryAgainButton1(imageUri: String?, infoFormViewModel: InfoFormViewModel, aiViewModel: AIViewModel) {

    val context = LocalContext.current
    var shouldRetry = remember { mutableStateOf(false) }

    Button(
        onClick = { shouldRetry.value = true }
    ) {
        Text("Try Again")
    }

    val imagePartState = remember { mutableStateOf<MultipartBody.Part?>(null) }

    LaunchedEffect(imageUri) {
        imagePartState.value = prepareFilePart("image", imageUri, context)
    }
    LaunchedEffect(shouldRetry) {
        if (shouldRetry.value) {
            try {
                val aiInput = AIInput(
                    os = infoFormViewModel.formState.os,
                    screenSize = infoFormViewModel.formState.screenSize,
                    fiveG = infoFormViewModel.formState.is5G,
                    internalMemory = infoFormViewModel.formState.internalMemory,
                    ram = infoFormViewModel.formState.ram,
                    battery = infoFormViewModel.formState.batteryInfo,
                    releaseYear = infoFormViewModel.formState.releaseYear,
                    daysUsed = infoFormViewModel.formState.daysUsed,
                    normalizedNewPrice = infoFormViewModel.formState.normalizedNewPrice.toDoubleOrNull()?.div(1000)?.toString() ?: "",
                    deviceBrand = infoFormViewModel.formState.deviceBrand,
                )

                if (imagePartState.value != null) {
                    aiViewModel.fetchPrediction(aiInput, imagePartState.value!!)
                } else {
                    Log.d("PredictionScreen", "Image Part is NULL")
                }
            } catch (e: Exception) {
                Log.e("PredictionScreen", "Error in prediction", e)
            } finally {
                shouldRetry.value = false
            }
        }
    }
}
