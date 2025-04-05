package com.example.ecopulse.ui.screens.screenfunctions

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecopulse.R
import com.example.ecopulse.ui.viewmodel.HealthTip

@Composable
fun SuggestionsBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 1.dp),
            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Absolute.SpaceEvenly
        ) {
            Image(
                painterResource(R.drawable.recycle),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "Things to know about",
                fontFamily = FontFamily(Font(R.font.quick_sand)),
                fontSize = 20.sp,
                color = Color(6, 20, 27 ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun SuggestionsCard(
    tip: HealthTip,
    onLearnMore: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(204, 208, 207)
        )
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Row(
                modifier = Modifier.padding(2.dp)

            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(tip.title, fontSize = 18.sp, fontFamily = FontFamily(Font(R.font.open_sans, FontWeight.ExtraBold)), color = Color(6, 20, 27 ))
                    Button(
                        onClick = {onLearnMore()},
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .width(120.dp),
                        colors = ButtonColors(
                            containerColor = Color(155, 168, 171 ),
                            contentColor = Color(6, 20, 27 ),
                            disabledContainerColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        )
                    ) {
                        Text(tip.buttonText, fontFamily = FontFamily(Font(R.font.open_sans)))
                    }
                }
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(tip.imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }
        }
    }
}