package com.example.sampleapplication.features.splash.presentation.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.sampleapplication.features.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
) {
    Column {
        Text(text = "Splash Screen")
        Button(onClick = {
            navigator.navigate(
                HomeScreenDestination()
            )
        }) {
            Text(text = "Go to Home")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenWithPreview() {
    SplashScreen(
        navigator = EmptyDestinationsNavigator
    )
}
