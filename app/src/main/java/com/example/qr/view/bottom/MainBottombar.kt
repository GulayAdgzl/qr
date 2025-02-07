package com.example.qr.view.bottom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.qr.R
import com.rahad.riobottomnavigation.composables.RioBottomNavItemData
import com.rahad.riobottomnavigation.composables.RioBottomNavigation


@Composable
fun MainBottombar() {
    // Constants for icon resources and labels
    val items = listOf(
        R.drawable.qr,
        R.drawable.history,

    )

    val labels = listOf(
        "Generate",
        "History",
    )

    // Use rememberSaveable to retain state across configuration changes
    var selectedIndex = rememberSaveable  { mutableIntStateOf(0) }

    // Create RioBottomNavItemData for the bottom navigation buttons
    val buttons = items.mapIndexed { index, iconData ->
        RioBottomNavItemData(
            imageVector = ImageVector.vectorResource(iconData),
            selected = index == selectedIndex.intValue,
            onClick = { selectedIndex.intValue = index },
            label = labels[index]
        )
    }

    // Main Scaffold setup
    Scaffold(
        bottomBar = {
            BottomNavigationBar(buttons = buttons)
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // Handle the screen content based on the selected index
        ScreenContent(selectedIndex.intValue, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun ScreenContent(selectedIndex: Int, modifier: Modifier = Modifier) {
    when (selectedIndex) {
        0 -> ShowText("Generate")
        1 -> ShowText("History")

    }
}

@Composable
fun ShowText(screenName: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text(screenName, style = MaterialTheme.typography.titleLarge)
    }

}


@Composable
fun BottomNavigationBar(buttons: List<RioBottomNavItemData>) {

    RioBottomNavigation(
        fabIcon = ImageVector.vectorResource(id = R.drawable.bottomqr),
        buttons = buttons,
        fabSize = 70. dp,
        barHeight = 70.dp,
        selectedItemColor = Color(0xFF7980FF),
        fabBackgroundColor = Color(0xFF7980FF)
    )
}
