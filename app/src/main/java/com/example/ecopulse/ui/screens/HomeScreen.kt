package com.example.ecopulse

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecopulse.ui.viewmodel.EcoCard
import com.example.ecopulse.ui.viewmodel.HomeViewModel
import com.example.ecopulse.routes.Screen
import com.example.ecopulse.ui.screens.screenfunctions.BottomSheetContent
import com.example.ecopulse.ui.screens.screenfunctions.SuggestionsBanner
import com.example.ecopulse.ui.screens.screenfunctions.SuggestionsCard
import com.example.ecopulse.ui.viewmodel.TopicDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(), navController: NavController) {

    val cardItems = viewModel.cardItems
    val healthTips = viewModel.healthTip

    val sheetState = rememberModalBottomSheetState()
    var selectedTopic by remember { mutableStateOf<TopicDetails?>(null) }
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val scrollableBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val context = LocalContext.current

    var isFabExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
            val nextIndex = listState.firstVisibleItemIndex + 1

            coroutineScope.launch {
                if (nextIndex < cardItems.size) {
                    listState.animateScrollToItem(nextIndex)
                } else {
                    // Smooth transition by scrolling back to the first card
                    listState.animateScrollToItem(0)
                }
            }
        }
    }
        LaunchedEffect(listState) {
            snapshotFlow { listState.firstVisibleItemScrollOffset }
                .collect { scrollOffset ->
                    coroutineScope.launch {
                        isFabExpanded = scrollOffset == 0  // Collapse text if scrolling down
                    }
                }
        }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(connection = scrollableBehavior.nestedScrollConnection),
        topBar = {
            HomeTopAppBar(scrollableBehavior, navController)
        },
        bottomBar = {
            HomeBottomAppBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(isFabExpanded = isFabExpanded, navController, context = context)
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(3.dp)
                .fillMaxWidth()
                .background(Color(242, 243, 241))
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 8.dp),
                state = listState
            ) {
                items(cardItems) { card ->
                    EcoBanner(card)
                }
            }

            SuggestionsBanner()
            healthTips.forEach { tip ->
                SuggestionsCard(tip) {
                    selectedTopic = viewModel.getTopicDetail(tip.title) // Fetch correct data
                    isBottomSheetOpen = true
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }
            }
        }
        if (isBottomSheetOpen && selectedTopic != null) {
            ModalBottomSheet(
                onDismissRequest = { isBottomSheetOpen = false },
                sheetState = sheetState,
                containerColor = Color(204, 208, 207 ),
                tonalElevation = 5.dp,
                modifier = Modifier.fillMaxHeight()
            ) {
                selectedTopic?.let { topic ->
                    BottomSheetContent(topic)
                }
            }
        }
    }
}


@Composable
fun EcoBanner(
    card: EcoCard,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .width(360.dp)
        ) {
            Image(
                painter = painterResource(card.imageResId),
                contentDescription = card.description,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Black,
                                Color.Transparent
                            ),
                            startX = 10f
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Column {
                        Text(card.title, fontFamily = FontFamily(Font(R.font.open_sans)), style = TextStyle(color = Color.White), fontSize = 16.sp)
                        Text(
                            card.description,
                            fontFamily = FontFamily(Font(R.font.open_sans)),
                            style = TextStyle(color = Color.White),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun HomeTopAppBar(scrollBehavior: TopAppBarScrollBehavior, navController: NavController) {
    TopAppBar(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(255, 255, 255, 255)
        ),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(R.drawable.recycling_earth), contentDescription = null)
                Text(
                    text = "ReCellMate",
                    style = TextStyle(fontFamily = FontFamily(Font(R.font.open_sans))),
                    fontSize = 20.sp,
                    color = Color(6, 20, 27 )
                )
            }

        },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = { navController.navigate(Screen.User.route) }) {
                Icon(
                    painter = painterResource(R.drawable.user),
                    contentDescription = null,
                    tint = Color(6, 20, 27 ),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 5.dp),
                )
            }
        }
    )
}

@Composable
fun HomeBottomAppBar(navController: NavController) {
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

@Composable
fun FloatingActionButton(isFabExpanded: Boolean, navController: NavController, context: Context) {
    val showPermissionDeniedDialog = remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            navController.navigate(Screen.ScanDevice.route)
        } else {
            showPermissionDeniedDialog.value = true
        }
    }
    if (showPermissionDeniedDialog.value) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog.value = false },
            title = { Text("Permission Required") },
            text = { Text("Please allow camera access in settings to scan devices.") },
            confirmButton = {
                Button(onClick = {
                    openAppSettings(context) // Open app settings
                    showPermissionDeniedDialog.value = false // Close dialog
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                Button(onClick = { showPermissionDeniedDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    FloatingActionButton(

        onClick = {
            val hasCameraPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
            if (hasCameraPermission) {
                navController.navigate(Screen.ScanDevice.route)
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        },
        shape = CircleShape,
        containerColor = Color(74, 92, 106 ),
        contentColor = Color(204, 208, 207 ),
        modifier = Modifier.padding(16.dp).shadow(elevation = 10.dp,
            ambientColor = Color(0f, 0f, 0f, 0.15f)),
        elevation = FloatingActionButtonDefaults.elevation(6.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .animateContentSize()
        ) {
            Icon(
                painter = painterResource(R.drawable.camera),
                contentDescription = "Scan",
                modifier = Modifier.size(30.dp)
            )
            AnimatedVisibility(visible = isFabExpanded) {
                Text(
                    text = "Scan Device",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}