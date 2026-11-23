package com.example.metrotest

import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metrotest.ui.theme.METROTESTTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.Check
import kotlin.math.absoluteValue
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateMapOf

@Composable
fun CarouselScreen(onNavigateToHome: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF5BD3), // #FF5BD3
                        Color(0xFF255EBD), // #255EBD  
                        Color(0xFF001041)  // #001041
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 0.dp)
        ) {

            // Main content header
            Pick11HeaderSection()

            Spacer(modifier = Modifier.height(24.dp))

            // Progress indicator section and match cards are now both part of MatchCardSection
            MatchCardSection()

            Spacer(modifier = Modifier.weight(1f))
        }

        // Bottom bar at the bottom of the screen
        BottomBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            selectedPicks = 0, // This should be dynamic based on user selections
            onBuyNowClick = onNavigateToHome
        )
    }
}

@Composable
fun CarouselScreenHeaderSection() {
    // This would normally be implemented with your actual header content
    // For this example, we'll just add a placeholder
    Text(
        text = "Header Section",
        style = MaterialTheme.typography.headlineMedium,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

@Composable
fun Pick11HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pick 11\nWIN ₦10 MILLION",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.4.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Predict the results of these 11 matches\nfor a chance to win",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun ProgressSection(current: Int = 1, total: Int = 11) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$current/$total",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontSize = 36.sp,
                lineHeight = 47.sp,
                letterSpacing = 0.4.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun MatchCardSection() {
    // Sample match data
    val matchesList = remember {
        listOf(
            MatchData(
                leagueName = "La Liga",
                homeTeam = "CA Osasuna",
                homeTeamLogo = R.drawable.team_ca_osasuna_icon,
                awayTeam = "Girona FC",
                awayTeamLogo = R.drawable.girona_fc_shield_1,
                matchDate = "13 Apr",
                matchTime = "13:00"
            ),
            MatchData(
                leagueName = "Premier League",
                homeTeam = "Arsenal",
                homeTeamLogo = R.drawable.team_ca_osasuna_icon, // Using placeholder
                awayTeam = "Chelsea",
                awayTeamLogo = R.drawable.girona_fc_shield_1, // Using placeholder
                matchDate = "14 Apr",
                matchTime = "15:30"
            ),
            MatchData(
                leagueName = "Serie A",
                homeTeam = "Juventus",
                homeTeamLogo = R.drawable.team_ca_osasuna_icon, // Using placeholder
                awayTeam = "Inter Milan",
                awayTeamLogo = R.drawable.girona_fc_shield_1, // Using placeholder
                matchDate = "15 Apr",
                matchTime = "20:45"
            ),
            MatchData(
                leagueName = "Bundesliga",
                homeTeam = "Bayern Munich",
                homeTeamLogo = R.drawable.team_ca_osasuna_icon, // Using placeholder
                awayTeam = "Dortmund",
                awayTeamLogo = R.drawable.girona_fc_shield_1, // Using placeholder
                matchDate = "16 Apr",
                matchTime = "18:30"
            ),
            MatchData(
                leagueName = "Ligue 1",
                homeTeam = "PSG",
                homeTeamLogo = R.drawable.team_ca_osasuna_icon, // Using placeholder
                awayTeam = "Marseille",
                awayTeamLogo = R.drawable.girona_fc_shield_1, // Using placeholder
                matchDate = "17 Apr",
                matchTime = "21:00"
            )
        )
    }

    // Pager state
    val pagerState = rememberPagerState(pageCount = { matchesList.size })

    // Current page for updating progress
    val currentPage = pagerState.currentPage

    // Keep track of selections for all matches
    val selectedOptions = remember { mutableStateMapOf<Int, String>() }

    // Update progress section
    ProgressSection(current = currentPage + 1, total = matchesList.size)

    // Horizontal pager for match cards with wheel-like animation
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { page ->
        // Calculate the absolute offset for this page from the current page
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        // Apply wheel-like animation transformations
        Box(
            modifier = Modifier
                .graphicsLayer {
                    // Scale down as it moves away
                    val scale = lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    scaleX = scale
                    scaleY = scale

                    // Move down and left as it's swiped away
                    translationY = lerp(
                        start = 0f,
                        stop = 50f,
                        fraction = pageOffset.coerceIn(0f, 1f)
                    )

                    translationX = lerp(
                        start = 0f,
                        stop = -30f,
                        fraction = pageOffset.coerceIn(0f, 1f)
                    )

                    // Add a slight rotation for more wheel effect
                    rotationZ = lerp(
                        start = 0f,
                        stop = -5f,
                        fraction = pageOffset.coerceIn(0f, 1f)
                    )

                    // Increase alpha for current page
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            val coroutineScope = rememberCoroutineScope()

            MatchCard(
                matchData = matchesList[page],
                selectedOption = selectedOptions[page],
                onSelectionMade = { option ->
                    // Save the selection for this page
                    selectedOptions[page] = option

                    coroutineScope.launch {
                        if (page < matchesList.size - 1) {
                            pagerState.animateScrollToPage(page + 1, animationSpec = spring())
                        }
                    }
                }
            )
        }
    }
}

// Data class for match information
data class MatchData(
    val leagueName: String,
    val homeTeam: String,
    val homeTeamLogo: Int,
    val awayTeam: String,
    val awayTeamLogo: Int,
    val matchDate: String,
    val matchTime: String
)

@Composable
fun MatchCard(
    matchData: MatchData,
    selectedOption: String? = null,
    onSelectionMade: (String) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // League name
            Text(
                text = matchData.leagueName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    letterSpacing = 0.4.sp
                ),
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )

            // Teams section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = matchData.homeTeamLogo),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = matchData.homeTeam,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Match time and date
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = matchData.matchDate,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = matchData.matchTime,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Normal
                    )
                }

                // Away team
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = matchData.awayTeamLogo),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = matchData.awayTeam,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Who will win section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Who will win?",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )

                // Betting options
                // No need for local state as it's passed from parent

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    BettingOption(
                        text = "Home",
                        modifier = Modifier.weight(1f),
                        isSelected = selectedOption == "Home",
                        onClick = {
                            onSelectionMade("Home")
                        }
                    )
                    BettingOption(
                        text = "Draw",
                        modifier = Modifier.weight(1f),
                        isSelected = selectedOption == "Draw",
                        onClick = {
                            onSelectionMade("Draw")
                        }
                    )
                    BettingOption(
                        text = "Away",
                        modifier = Modifier.weight(1f),
                        isSelected = selectedOption == "Away",
                        onClick = {
                            onSelectionMade("Away")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BettingOption(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {}
) {
    // Selected colors
    val selectedBackgroundColor = Color(0xFFFF5BD3)
    val selectedCheckColor = Color(0xFF001041)
    val defaultBackgroundColor = Color.White.copy(alpha = 0.9f)
    val defaultTextColor = Color(0xFF0BBBE6)

    Card(
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) selectedBackgroundColor else defaultBackgroundColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {


                // Show checkmark if selected
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = selectedCheckColor,
                        modifier = Modifier.size(16.dp)
                    )
                }else{
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp
                        ),
                        color = if (isSelected) Color.White else defaultTextColor,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(end = if (isSelected) 4.dp else 0.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselScreenPreview() {
    METROTESTTheme(darkTheme = true) {
        CarouselScreen()
    }
}
