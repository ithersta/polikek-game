package com.ithersta.polikekgame

import com.ithersta.polikekgame.entities.GameConfig
import com.ithersta.polikekgame.entities.GameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KotlinLogging

context(GameConfig)
class GameService(private val repository: GameStateRepository) {
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Default)
    private val logger = KotlinLogging.logger { }

    fun get(gameIdentifier: GameIdentifier): GameState? {
        return repository.get(gameIdentifier)
    }

    fun start(gameIdentifier: GameIdentifier): GameState {
        val state = GameState.fromConfig()
        repository.set(gameIdentifier, state)
        return state
    }

    fun buy(gameIdentifier: GameIdentifier): GameState? {
        val state = repository.get(gameIdentifier)?.afterPurchase() ?: return null
        repository.set(gameIdentifier, state)
        return state
    }

    fun sell(gameIdentifier: GameIdentifier): GameState? {
        val state = repository.get(gameIdentifier)?.afterSale() ?: return null
        repository.set(gameIdentifier, state)
        if (state.isDead) {
            logger.info { "${identities[gameIdentifier.userId]} scored ${state.stats.purchases}" }
            coroutineScope.launch {
                telegramBot.setGameScore(
                    userId = gameIdentifier.userId,
                    score = state.stats.purchases,
                    messageId = gameIdentifier.messageId,
                    chatId = gameIdentifier.chatId,
                    inlineMessageId = gameIdentifier.inlineMessageId
                )
            }
        }
        return state
    }
}
