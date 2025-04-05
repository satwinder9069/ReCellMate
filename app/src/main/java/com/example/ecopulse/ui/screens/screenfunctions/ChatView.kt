package com.example.ecopulse.ui.screens.screenfunctions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecopulse.R
import com.example.ecopulse.data.models.GeminiMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChatBubble(message: GeminiMessage) {
    val isUser = message.role == "user"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    ) {
        Text(
            text =  message.parts.joinToString(" ") { it.text },
            fontFamily = FontFamily(
                Font(R.font.quick_sand)
            ),
            fontSize = 16.sp,
            color = if (isUser) Color(204, 208, 207) else Color(6, 20, 27 ),
            modifier = Modifier
                .background(
                    if (isUser) Color(74, 92, 106 ) else Color(155, 168, 171),
                    shape = if (isUser) RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 0.dp
                    )
                    else RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 16.dp
                    )
                )
                .padding(10.dp)
        )

    }
}
@Composable
fun TypingIndicator() {
    val dotCount = 3
    val animatedValues = remember { List(dotCount) { Animatable(0f) } }

    LaunchedEffect(Unit) {
        animatedValues.forEachIndexed { index, animatable ->
            launch {
                delay(index * 300L) // Staggered effect
                animatable.animateTo(
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 600, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    )
                )
            }
        }
    }

    Row(modifier = Modifier.padding(8.dp)) {
        animatedValues.forEach { animatable ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(horizontal = 2.dp)
                    .background(Color.Gray.copy(alpha = animatable.value), shape = CircleShape)
            )
        }
    }
}

