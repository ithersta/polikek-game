package ui

import GameClient
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun App(client: GameClient) {
    val state by client.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    when (state) {
        null -> StartScreen { coroutineScope.launch { client.start() } }
    }
}
