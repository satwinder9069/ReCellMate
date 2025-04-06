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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun PredictionScreen(
    imageUri: String?,
    infoFormViewModel: InfoFormViewModel,
    navController: NavController
) {
    val api = RetrofitInstance.api
    val repository = AIRepository(api)
    val aiViewModel: AIViewModel = viewModel(
        factory = AIViewModelFactory(repository)
    )
    val scope = rememberCoroutineScope()
    val result = aiViewModel.predictionResult
    val isLoading = aiViewModel.isLoading
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color(204, 208, 207)
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
                                tint = Color(6, 20, 27),
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
                                tint = Color(6, 20, 27),
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

                        val aiInput = AIInput(
                            os = infoFormViewModel.formState.os,
                            screenSize = infoFormViewModel.formState.screenSize,
                            fiveG = infoFormViewModel.formState.is5G,
                            internalMemory = infoFormViewModel.formState.internalMemory,
                            ram = infoFormViewModel.formState.ram,
                            battery = infoFormViewModel.formState.batteryInfo,
                            releaseYear = infoFormViewModel.formState.releaseYear,
                            daysUsed = infoFormViewModel.formState.daysUsed,
                            normalizedNewPrice = infoFormViewModel.formState.normalizedNewPrice.toDoubleOrNull()
                                ?.div(1000)?.toString() ?: "",
                            deviceBrand = infoFormViewModel.formState.deviceBrand,
                        )

                        if (imagePartState.value != null) {
                            aiViewModel.fetchPrediction(aiInput, imagePartState.value!!)
                        } else {
                            Log.d("PredictionScreen", "Image Part is NULL")
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(74, 92, 106)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    "Predict",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(204, 208, 207)
                )
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
                                    fontFamily = FontFamily(Font(R.font.open_sans)),
                                    color = Color(0xFF2C3E50)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "✅ Resale Price: ${prediction.finalAdjustedPrice} ₹",
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(Font(R.font.open_sans)),
                                    color = Color(6, 20, 27)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📈 Predicted Price: ${prediction.predictedPrice}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontFamily = FontFamily(Font(R.font.open_sans)),
                                    color = Color(37, 55, 69)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Column (
                        modifier = Modifier.align(Alignment.Start).padding(start = 10.dp)
                    ) {
                        Text("\uD83D\uDCA1 NOTE:", fontSize = 20.sp, fontFamily = FontFamily(Font(R.font.open_sans)))
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth().padding(top = 8.dp)
                            .shadow(10.dp, shape = RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(193, 201, 208, 255))
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.SpaceAround,
                            ) {
                                Text(
                                    text = "Resale Price: ",
                                    fontSize = 16.sp,
                                    color = Color(6, 20, 27),
                                    fontFamily = FontFamily(Font(R.font.open_sans)),
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = "The final price after adjusting the predicted value based on the device's condition.",
                                    fontFamily = FontFamily(
                                        Font(R.font.open_sans)
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(37, 55, 69)
                                )
                            }
                            Column(
                                modifier = Modifier.padding(8.dp),
                                verticalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Text(
                                    text = "Predicted Price: ",
                                    fontSize = 16.sp,
                                    color = Color(6, 20, 27),
                                    fontFamily = FontFamily(Font(R.font.open_sans)),
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Text(
                                    text = "The base value of the device estimated using its technical and usage details.",
                                    fontFamily = FontFamily(
                                        Font(R.font.open_sans)
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(37, 55, 69)
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}