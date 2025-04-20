package com.skipper.taskManager.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.skipper.taskManager.R
import com.skipper.taskManager.utils.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.settings)) })
        }
    ) { padding ->
        SettingsScreenContent(modifier = Modifier.padding(padding)) {}
    }
}

@Composable
fun SettingsScreenContent(modifier: Modifier, onColorChange: (Color) -> Unit) {
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .then(modifier), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.select_primary_color),
            style = MaterialTheme.typography.titleMedium
        )
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