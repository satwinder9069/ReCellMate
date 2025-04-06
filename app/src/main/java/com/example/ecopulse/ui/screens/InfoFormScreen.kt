package com.example.ecopulse.ui.screens

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecopulse.routes.Screen
import com.example.ecopulse.utils.InfoForm
import com.example.ecopulse.utils.InfoFormViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoFormScreen(
    imageUri: String?,
    navController: NavController,
    viewModel: InfoFormViewModel
) {
    val context = LocalContext.current
    val uri = imageUri?.let { Uri.parse(it) }

    val capturedImageFile = viewModel.capturedImageFile

    val formState = viewModel.formState
    val errors = remember { mutableStateMapOf<String, String>() }
    val scrollState = rememberScrollState()

    var is5G = listOf("yes", "no")
    var device_brands = listOf(
        "Honor", "Others", "HTC", "Huawai", "Infinix", "Lava", "Lenovo", "LG", "Meizu",
        "Micromax", "Motorola", "Nokia", "OnePlus", "Oppo", "Realme", "Samsung", "Vivo",
        "Xiaomi", "ZTE", "Apple", "Asus", "Coolpad", "Acer", "Alcatel", "BlackBerry",
        "Celkon", "Gionee", "Google", "Karbonn", "Microsoft", "Panasonic", "Sony", "Spice", "XOLO"
    )
    device_brands = device_brands.sorted()
    var deviceBrandExpanded by remember { mutableStateOf(false) }
    var selectedBrand by remember { mutableStateOf(formState.deviceBrand) }

    var is5Gexpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(formState.is5G) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFFAFAFA)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(scrollState)

        ) {
            capturedImageFile?.let {
                Log.d("URI", "Captured Image, $uri")
                Log.d("viewModel captured Image.", "Captured Image, ${viewModel.capturedImageFile}")
                Image(
                    painter = rememberAsyncImagePainter(model = it),
                    contentDescription = "Captured Image",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = deviceBrandExpanded,
                onExpandedChange = { deviceBrandExpanded = !deviceBrandExpanded },
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    label = { Text(text = "Device Brand") },
                    value = selectedBrand,
                    onValueChange = {},
                    shape = RoundedCornerShape(16.dp),
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceBrandExpanded) },
                )
                ExposedDropdownMenu(expanded = deviceBrandExpanded, onDismissRequest = {
                    deviceBrandExpanded = false
                }) {
                    device_brands.forEachIndexed { index, text ->
                        DropdownMenuItem(text = { Text(text = text) }, onClick = {
                            selectedBrand = device_brands[index]
                            viewModel.formState = viewModel.formState.copy(deviceBrand = selectedBrand)
                            deviceBrandExpanded = false
                        }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )

                    }
                }
            }
            OutlinedTextField(
                value = formState.model,
                onValueChange = {
                    viewModel.formState = viewModel.formState.copy(model = it.trim())
                    errors["Model"] = if (it.isBlank()) "Model is Required" else ""
                },
                label = { Text("Model (e.g., Galaxy S21, iPhone 13)") },
                isError = errors["Model"]?.isNotEmpty() == true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            if (errors["Model"]?.isNotEmpty() == true) {
                Text(text = errors["name"] ?: "", color = Color.Red)
            }

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = formState.internalMemory,
                    onValueChange = {
                        viewModel.formState = viewModel.formState.copy(internalMemory = it.trim())
                        errors["Internal Memory"] =
                            if (it.isBlank()) "Internal Memory is Required" else ""
                    },
                    label = { Text("Internal Memory (GB)") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = errors["Internal Memory"]?.isNotEmpty() == true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                if (errors["Internal Memory"]?.isNotEmpty() == true) {
                    Text(text = errors["Internal Memory"] ?: "", color = Color.Red)
                }
                OutlinedTextField(
                    value = formState.ram,
                    onValueChange = {
                        viewModel.formState = viewModel.formState.copy(ram = it.trim())

                        errors["ram"] = if (it.isBlank()) "Field cannot be empty" else ""
                    },
                    label = { Text("RAM (GB)") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    isError = errors["ram"]?.isNotEmpty() == true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth().height(60.dp)
                )
                if (errors["ram"]?.isNotEmpty() == true) {
                    Text(text = errors["ram"] ?: " ",
                        color = if (errors["ram"]?.isNotEmpty() == true) Color.Red else Color.Transparent,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            OutlinedTextField(
                value = formState.batteryInfo,
                onValueChange = {
                    viewModel.formState = viewModel.formState.copy(batteryInfo = it.trim())

                    errors["battery"] = if (it.isBlank()) "Field cannot be empty" else ""
                },
                label = { Text("Battery Info (mAh)") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                isError = errors["ram"]?.isNotEmpty() == true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            if (errors["battery"]?.isNotEmpty() == true) {
                Text(text = errors["battery"] ?: "", color = Color.Red)
            }

            OutlinedTextField(
                value = formState.screenSize,
                onValueChange = {
                    viewModel.formState = viewModel.formState.copy(screenSize = it)
                    errors["screenSize"] = if (it.isBlank()) "Screen Size is required!" else ""
                },
                label = { Text("Screen Size (inches)") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                isError = errors["screenSize"]?.isNotEmpty() == true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            if (errors["screenSize"]?.isNotEmpty() == true) {
                Text(text = errors["screenSize"] ?: "", color = Color.Red)
            }

            OutlinedTextField(
                value = formState.os,
                onValueChange = {
                    viewModel.formState = viewModel.formState.copy(os = it.trim())
                    errors["os"] = if (it.isBlank()) "Field cannot be empty" else ""
                },
                label = { Text("Operating System (Android, iOS)") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                isError = errors["os"]?.isNotEmpty() == true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            if (errors["os"]?.isNotEmpty() == true) {
                Text(text = errors["os"] ?: "", color = Color.Red)
            }

            ExposedDropdownMenuBox(
                expanded = is5Gexpanded,
                onExpandedChange = { is5Gexpanded = !is5Gexpanded },
            ) {
                OutlinedTextField(modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    label = { Text(text = "is Device 5G?") },
                    value = selectedOption,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = is5Gexpanded) })
                ExposedDropdownMenu(expanded = is5Gexpanded, onDismissRequest = {
                    is5Gexpanded = false
                }) {
                    is5G.forEachIndexed { index, text ->
                        DropdownMenuItem(text = { Text(text = text) }, onClick = {
                            selectedOption = is5G[index]
                            viewModel.formState = viewModel.formState.copy(is5G = selectedOption)
                            is5Gexpanded = false
                        }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )

                    }
                }

            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = formState.releaseYear,
                    onValueChange = {
                        viewModel.formState = viewModel.formState.copy(releaseYear = it)
                        errors["releaseYear"] = if (it.isBlank()) "Field cannot be empty" else ""
                    },
                    label = { Text("Release Year") },

                    isError = errors["releaseYear"]?.isNotEmpty() == true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
                if (errors["releaseYear"]?.isNotEmpty() == true) {
                    Text(text = errors["releaseYear"] ?: "", color = Color.Red)
                }

                OutlinedTextField(
                    value = formState.daysUsed,
                    onValueChange = {
                        viewModel.formState = viewModel.formState.copy(daysUsed = it.trim())
                        errors["daysUsed"] = if (it.isBlank()) "Field cannot be empty" else ""
                    },
                    label = { Text("Days Used") },
                    isError = errors["daysUsed"]?.isNotEmpty() == true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                )
                if (errors["daysUsed"]?.isNotEmpty() == true) {
                    Text(text = errors["daysUsed"] ?: "", color = Color.Red)
                }
            }
            OutlinedTextField(
                value = formState.normalizedNewPrice,
                onValueChange = {
                    viewModel.formState = viewModel.formState.copy(normalizedNewPrice = it.trim())
                    errors["normalizedNewPrice"] = if (it.isBlank()) "Field cannot be empty" else ""
                },
                label = { Text("New Unit Price") },
                isError = errors["normalizedNewPrice"]?.isNotEmpty() == true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
            if (errors["normalizedNewPrice"]?.isNotEmpty() == true) {
                Text(text = errors["normalizedNewPrice"] ?: "", color = Color.Red)
            }

            ElevatedButton(
                onClick = {
                    Log.d("DEBUG", "Selected Brand: ${viewModel.formState.deviceBrand}")

                    if (viewModel.validateForm()) {
                        val imageFile = viewModel.capturedImageFile
                        if (imageFile != null) {
                            val encodedPath = Uri.encode(imageFile.absolutePath)
                            navController.navigate(Screen.PredictionScreen.createRoute(encodedPath))
                        } else {
                            Toast.makeText(context, "Please capture image first!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (viewModel.validateForm()) Color(74, 92, 106) else Color(204, 208, 207 ),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Submit", color = Color.White, fontSize = 18.sp)

            }
        }
    }
}