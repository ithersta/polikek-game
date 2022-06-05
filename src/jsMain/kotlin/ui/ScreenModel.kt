package ui

import GameClient
import TransferGameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ScreenState(
    val gameState: TransferGameState? = null,
    val loadingStart: Boolean = false,
    val loadingBuy: Boolean = false,
    val loadingSell: Boolean = false
)

class ScreenModel(private val client: GameClient) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val gameState = MutableStateFlow<TransferGameState?>(null)
    private val loadingStart = MutableStateFlow(false)
    private val loadingBuy = MutableStateFlow(false)
    private val loadingSell = MutableStateFlow(false)

    val state = combine(gameState, loadingStart, loadingBuy, loadingSell) { gameState, loadingStart, loadingBuy, loadingSell ->
        ScreenState(gameState, loadingStart, loadingBuy, loadingSell)
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(5000L), ScreenState())

    fun start() = coroutineScope.launch {
        if (isLoading()) return@launch
        loadingStart.value = true
        gameState.value = client.start()
        loadingStart.value = false
    }

    fun buy() = coroutineScope.launch {
        if (isLoading()) return@launch
        loadingBuy.value = true
        gameState.value = client.buy()
        loadingBuy.value = false
    }

    fun sell() = coroutineScope.launch {
        if (isLoading()) return@launch
        loadingSell.value = true
        gameState.value = client.sell()
        loadingSell.value = false
    }

    fun isLoading(): Boolean {
        return loadingStart.value || loadingBuy.value || loadingSell.value
    }

    suspend fun fetch() {
        gameState.value = client.get()
    }
}
