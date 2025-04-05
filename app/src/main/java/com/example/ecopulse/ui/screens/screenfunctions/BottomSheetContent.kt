package com.example.ecopulse.ui.screens.screenfunctions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecopulse.R
import com.example.ecopulse.ui.viewmodel.TopicDetails

@Composable
fun BottomSheetContent(topic: TopicDetails) {

    Scaffold(
        modifier = Modifier.fillMaxHeight()
    ) { padding ->

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = topic.title,
                fontSize = 24.sp,
                color = Color(6, 20, 27),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = topic.subtitle,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.open_sans)),
                fontWeight = FontWeight.ExtraBold,
                color = Color(65, 104, 134, 255),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = topic.description,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.open_sans)),
                color = Color(74, 92, 106),
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(topic.imageRes) { imageResId ->
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Topic Image",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .shadow(elevation = 10.dp)

                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Effects:",
                fontFamily = FontFamily(Font(R.font.open_sans)),
                fontSize = 18.sp,
                color = Color(6, 20, 27),
                fontWeight = FontWeight.Bold
            )

            topic.effects.forEach {
                Text(
                    text = "• $it",
                    fontFamily = FontFamily(Font(R.font.open_sans)),
                    fontSize = 16.sp,
                    color = Color(74, 92, 106)
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 1.dp,
                color = Color(6, 20, 27)
            )
            Text(
                text = "\uD83D\uDCA1 Prevention Tips:", fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.open_sans)),
                color = Color(6, 20, 27),
                fontWeight = FontWeight.Bold
            )
            topic.preventionTips.forEach {
                Text(
                    text = "✓ $it",
                    fontFamily = FontFamily(Font(R.font.open_sans)),
                    fontSize = 16.sp, color = Color(74, 92, 106)
                )
            }

        }
    }

}