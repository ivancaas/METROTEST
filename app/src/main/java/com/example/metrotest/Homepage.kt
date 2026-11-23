package com.example.metrotest

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue
import com.example.metrotest.ui.theme.METROTESTTheme
import com.example.metrotest.ui.theme.PlayKingBackground
import com.example.metrotest.ui.theme.PlayKingGradientEnd
import com.example.metrotest.ui.theme.PlayKingGradientStart
import com.example.metrotest.ui.theme.PlayKingPrimary

@Composable
fun HomepageScreen(onNavigateToCarousel: () -> Unit = {}) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF250041)),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        item {
            BannerSection()
        }
        item {
            TopLinksSection()
        }
        item {
            LatestWinnersSection()
        }
        item {
            TopTrendingGamesSection()
        }
    }
}

@Composable
fun BannerSection() {
    // Create a list of banner items (we'll use 3 initial items and repeat them for infinite effect)
    val bannerItems = remember {
        listOf(
            BannerItem.MainBanner,
            BannerItem.MainBanner,
            BannerItem.MainBanner,
        )
    }

    // For infinite paging, we use a large initial page in the middle of a very large range
    val pageCount = Int.MAX_VALUE
    val initialPage = pageCount / 2
    val pagerState = rememberPagerState(initialPage = initialPage) { pageCount }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 40.dp)
    ) { page ->
        // Get the actual item (mod by the real item count)
        val item = bannerItems[page % bannerItems.size]

        // Calculate the page offset for animation
        val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                ).absoluteValue

        // Apply transformations based on page offset
        Box(
            modifier = Modifier
                .graphicsLayer {
                    // Scale items based on their distance from the center
                    val scale = lerp(
                        start = 0.89f,  // Increased minimum scale for better visibility
                        stop = 1f,     // Scale for the item in focus
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    scaleX = scale
                    scaleY = scale

                    // Adjust alpha to make focused item more prominent
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            when (item) {
                BannerItem.MainBanner -> MainBannerCard()
            }
        }
    }
}

sealed class BannerItem {
    data object MainBanner : BannerItem()
}

@Composable
fun MainBannerCard() {
    Card(
        modifier = Modifier
            .width(360.dp)
            .height(280.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PlayKingGradientStart, PlayKingGradientEnd)
                    )
                )
        ) {
            // Background image
            Image(
                painter = painterResource(id = com.example.metrotest.R.drawable.gaming_media_hub_banner),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.8f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Exclusive Offer badge
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFF6B5BFF),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Exclusive Offer",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column {
                    Text(
                        text = "BET N100\nGET 100\nFREE SPINS",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontFamily = FontFamily.Default,
                            fontSize = 36.sp,
                            lineHeight = 40.sp
                        ),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { /* Play action */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PlayKingPrimary
                        ),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .width(140.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Play Now",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderBannerCard() {
    Card(
        modifier = Modifier
            .width(240.dp)  // Increased width for better visibility
            .height(200.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(PlayKingGradientStart, PlayKingGradientEnd)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder for image icon
            Icon(
                painter = painterResource(id = com.example.metrotest.R.drawable.banner_icon_1),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.White.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
fun TopLinksSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 32.dp)
    ) {
        // First row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopLinkCard(
                image = com.example.metrotest.R.drawable.homepage_toplink_casino_games_tile,
                title = "GAMES",
                modifier = Modifier.weight(1f)
            )
            TopLinkCard(
                image = com.example.metrotest.R.drawable.homepage_toplink_tile_chatgpt,
                title = "CASINO",
                modifier = Modifier.weight(1f)
            )
            TopLinkCard(
                image = com.example.metrotest.R.drawable.homepage_toplinks_tile_virtuals,
                title = "LIVE CASINO",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Second row - only show one visible item as per Figma
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TopLinkCard(
                image = com.example.metrotest.R.drawable.homepage_toplink_tile_date_may21_2025,
                title = "PICK11",
                modifier = Modifier.weight(1f)
            )
            // Empty spaces for invisible items
            Spacer(modifier = Modifier.weight(2f))
        }
    }
}

@Composable
fun TopLinkCard(
    image: Int,
    title: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4A2C61)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Default,
                    fontSize = 16.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun LatestWinnersSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Text(
            text = "Latest Winners",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(5) { index ->
                WinnerCard(index)
            }
        }
    }
}

@Composable
fun WinnerCard(index: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    // Create colorful backgrounds for different users
                    when (index % 4) {
                        0 -> Color(0xFFFF6B6B) // Red
                        1 -> Color(0xFF4ECDC4) // Teal
                        2 -> Color(0xFFFFD93D) // Yellow
                        else -> Color(0xFF6BCF7F) // Green
                    }
                )
        ) {
            // Placeholder game logo instead of person icon
            Image(
                painter = painterResource(
                    when (index % 4) {
                        0 -> com.example.metrotest.R.drawable.homepage_carousel_cash_volt
                        1 -> com.example.metrotest.R.drawable.homepage_carousel_aviator_screenshot
                        2 -> com.example.metrotest.R.drawable.homepage_carousel_frame1_screenshot
                        else -> com.example.metrotest.R.drawable.homepage_carousel_cash_volt
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = "ProfileN...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )
            Text(
                text = "N2,620",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun TopTrendingGamesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Text(
            text = "Top 10 Trending Games",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                GameCard(
                    rank = "1",
                    title = "Aviator",
                    players = "642",
                    image = com.example.metrotest.R.drawable.homepage_carousel_frame1_screenshot,
                    isRanked = true
                )
            }
            item {
                GameCard(
                    rank = "2",
                    title = "Sweet bonanza",
                    players = "308",
                    image = com.example.metrotest.R.drawable.homepage_carousel_cash_volt,
                    isRanked = true
                )
            }
            item {
                GameCard(
                    rank = "3",
                    title = "Aviator",
                    players = "642",
                    image = com.example.metrotest.R.drawable.homepage_carousel_aviator_screenshot,
                    isRanked = true
                )
            }
            item {
                GameCard(
                    rank = "",
                    title = "Game\nTitle",
                    players = "",
                    image = null,
                    isRanked = false
                )
            }
        }
    }
}

@Composable
fun GameCard(
    rank: String,
    title: String,
    players: String,
    image: Int?,
    isRanked: Boolean
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(240.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    )
                )
        ) {
            if (image != null && isRanked) {
                // Background image
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                )
                            )
                        )
                )

                // Rank badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = rank,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FontFamily.Default,
                            fontSize = 20.sp
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )

                    if (isRanked && players.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Text(
                                text = players,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NavigationButtonSection(onNavigateToCarousel: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onNavigateToCarousel,
            colors = ButtonDefaults.buttonColors(
                containerColor = PlayKingPrimary
            ),
            shape = RoundedCornerShape(100.dp),
            modifier = Modifier
                .width(180.dp)
                .height(48.dp)
        ) {
            Text(
                text = "Go to Pick11",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    METROTESTTheme(darkTheme = true) {
        HomepageScreen()
    }
}
