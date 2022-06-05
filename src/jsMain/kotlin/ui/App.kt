package ui

import GameClient
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun App(screenModel: ScreenModel) {
    MaterialTheme {
        val state by screenModel.state.collectAsState()
        when (val gameState = state.gameState) {
            null -> StartScreen(state.loadingStart) { screenModel.start() }
            else -> GameScreen(
                state = gameState,
                loadingBuy = state.loadingBuy,
                loadingSell = state.loadingSell,
                buy = { screenModel.buy() },
                sell = { screenModel.sell() }
            )
        }
    }
}
