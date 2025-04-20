package com.skipper.taskManager.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.skipper.taskManager.utils.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(navController: NavController) {
    SettingsScreenContent {}
}

@Composable
fun SettingsScreenContent(onColorChange: (Color) -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier
        .padding(top = 56.dp, start = 8.dp, end = 8.dp)
        .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Select Primary Color", style = MaterialTheme.typography.titleMedium)
        Row {
            listOf(Color.Red, Color.Blue, Color.Green).forEach { color ->
                Button(
                    onClick = {
                        onColorChange(color)
                        CoroutineScope(Dispatchers.IO).launch {
                            SettingsDataStore.savePrimaryColor(context, color.toArgb())
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = color),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(" ", modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}