package com.example.metrotest

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metrotest.ui.theme.METROTESTTheme

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedPicks: Int = 0,
    onBuyNowClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF001041), // #001041 - dark blue at top
                        Color(0xFF255EBD), // #255EBD - medium blue
                        Color(0xFFFF5BD3)  // #FF5BD3 - pink at bottom
                    ),
                    start = androidx.compose.ui.geometry.Offset.Zero,
                    end = androidx.compose.ui.geometry.Offset.Infinite
                ),
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Your Picks section
            YourPicksSection(
                selectedPicks = selectedPicks,
                modifier = Modifier.weight(1f)
            )

            // Buy Now Button
            BuyNowButton(
                onClick = onBuyNowClick
            )
        }
    }
}

@Composable
private fun YourPicksSection(
    selectedPicks: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .alpha(0.4f),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Number indicator - using CircleShape instead of RoundedCornerShape
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = Color(0xFFFF5BD3),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = selectedPicks.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // "Your Picks" text
        Text(
            text = "Your Picks",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                letterSpacing = 0.4.sp
            ),
            color = Color.White.copy(alpha = 0.8f),
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun BuyNowButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(40.dp),
        shape = RoundedCornerShape(360.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color(0xFFE3F1FD).copy(alpha = 0.3f)
        ),
        contentPadding = PaddingValues(horizontal = 30.dp, vertical = 2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Buy Now",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 10.sp
                ),
                color = Color(0xFFE3F1FD).copy(alpha = 0.8f),
                fontWeight = FontWeight.Normal
            )
            Text(
                text = "₦200",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFFE3F1FD).copy(alpha = 0.8f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    METROTESTTheme(darkTheme = true) {
        Column {
            Spacer(modifier = Modifier.height(200.dp))
            BottomBar(
                selectedPicks = 0
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarWithPicksPreview() {
    METROTESTTheme(darkTheme = true) {
        Column {
            Spacer(modifier = Modifier.height(200.dp))
            BottomBar(
                selectedPicks = 5
            )
        }
    }
}
