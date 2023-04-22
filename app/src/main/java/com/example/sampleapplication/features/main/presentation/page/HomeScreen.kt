package com.example.sampleapplication.features.main.presentation.page

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    Text(text = "Home Screen")
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(EmptyDestinationsNavigator)
}
