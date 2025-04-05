package com.example.ecopulse.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.ecopulse.R
import com.example.ecopulse.utils.InfoForm
import com.example.ecopulse.utils.InfoFormViewModel
//import com.example.ecopulse.utils.UserFormState


@Composable
fun DeviceInfoScreen(imageUri : String?, navController: NavController , viewModel: InfoFormViewModel = viewModel()) {

    val formState = viewModel.formState
    val errorMessage = viewModel.errorMessage

    var device_brands = listOf(
        "Honor", "Others", "HTC", "Huawai", "Infinix", "Lava", "Lenovo", "LG", "Meizu",
        "Micromax", "Motorola", "Nokia", "OnePlus", "Oppo", "Realme", "Samsung", "Vivo",
        "Xiaomi", "ZTE", "Apple", "Asus", "Coolpad", "Acer", "Alcatel", "BlackBerry",
        "Celkon", "Gionee", "Google", "Karbonn", "Microsoft", "Panasonic", "Sony", "Spice", "XOLO"
    )
    device_brands = device_brands.sorted()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {  }


}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceInfoScreen1(imageUri: String?, navController: NavController) {
    var formState = remember { mutableStateOf(InfoForm()) }
    var isExpanded = remember { mutableStateOf(false) }
    var device_brands = listOf(
        "Honor",
        "Others",
        "HTC",
        "Huawai",
        "Infinix",
        "Lava",
        "Lenovo",
        "LG",
        "Meizu",
        "Micromax",
        "Motorola",
        "Nokia",
        "OnePlus",
        "Oppo",
        "Realme",
        "Samsung",
        "Vivo",
        "Xiaomi",
        "ZTE",
        "Apple",
        "Asus",
        "Coolpad",
        "Acer",
        "Alcatel",
        "BlackBerry",
        "Celkon",
        "Gionee",
        "Google",
        "Karbonn",
        "Microsoft",
        "Panasonic",
        "Sony",
        "Spice",
        "XOLO"
    )
    device_brands = device_brands.sorted()

    val is5G = listOf("yes", "no")

    var deviceBrandExpanded by remember { mutableStateOf(false) }
    var is5GExpanded by remember { mutableStateOf(is5G[0]) }

    var selectedText by remember { mutableStateOf(device_brands[0]) }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            ExposedDropdownMenuBox(
                expanded = deviceBrandExpanded,
                onExpandedChange = { deviceBrandExpanded = !deviceBrandExpanded },
            ) {
                OutlinedTextField(modifier = Modifier.menuAnchor(),
                    label = { Text(text = "Device Brand") },
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceBrandExpanded) })
                ExposedDropdownMenu(expanded = deviceBrandExpanded, onDismissRequest = {
                    deviceBrandExpanded = false
                }) {
                    device_brands.forEachIndexed { index, text ->
                        DropdownMenuItem(text = { Text(text = text) }, onClick = {
                            selectedText = device_brands[index]
                            deviceBrandExpanded = false
                        }, contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )

                    }
                }

            }

        }
    }

}
