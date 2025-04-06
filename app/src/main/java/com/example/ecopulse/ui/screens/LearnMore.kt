package com.example.ecopulse.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecopulse.ui.viewmodel.ChatViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecopulse.R
import com.example.ecopulse.data.api.RetrofitChatInstance
import com.example.ecopulse.data.models.GeminiMessage
import com.example.ecopulse.data.repository.GeminiRepository
import com.example.ecopulse.ui.screens.screenfunctions.ChatBubble
import com.example.ecopulse.ui.screens.screenfunctions.TypingIndicator
import com.example.ecopulse.ui.viewmodel.ChatViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnMore(navController: NavController) {

    val repository = GeminiRepository(RetrofitChatInstance.geminiApi)
    val chatViewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(repository))
    val chatHistory by chatViewModel.chatHistory.observeAsState(emptyList())

    var userInput by remember { mutableStateOf("") }

    val isLoading by chatViewModel.isLoading.collectAsState()

    val lazyListState = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState)

    Scaffold(
        modifier = Modifier,
        containerColor = Color(204, 208, 207 ),
        topBar = {
            TopAppBar(
                title = { Text(text = "Hi User",
                    fontFamily = FontFamily(
                        Font(R.font.open_sans, FontWeight.ExtraBold)
                    )) },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(155, 168, 171 )
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_left),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            MessageInput(
                userInput = userInput,
                onMessageChange = { userInput = it },
                onSendMessage = {
                    if (userInput.isNotBlank()) {
                        chatViewModel.sendMessage(userInput)
                        userInput = ""
                    }
                })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(1.dp),
            flingBehavior = flingBehavior
        ) {
            items(chatHistory) { message ->
                ChatBubble(message)
            }

            if (isLoading) {
                item { TypingIndicator() }
            }
        }

    }

}

@Composable
fun MessageInput(
    userInput: String,
    onMessageChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .background(
                    color = Color(204, 208, 207 ),
                    shape = RoundedCornerShape(50.dp)
                )
                .shadow(2.dp, shape = RoundedCornerShape(50.dp))
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = userInput,
                onValueChange = { onMessageChange(it) },
                placeholder = {
                    Text("Ask something...", color = Color(0xFF4A5C6A ))
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color(0xFF06141B),
                    unfocusedTextColor = Color(0xFF06141B),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.weight(1f)
                    .padding(start = 8.dp),
                maxLines = 1,
                singleLine = true
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(74, 92, 106), shape = CircleShape)
                    .clickable {
                        if (userInput.isNotBlank()) {
                            onSendMessage()
                            onMessageChange("")
                        }

                    }
            ) {
                Icon(

                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFCCD0CF)
                )
            }

        }
    }
}