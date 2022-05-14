package com.ithersta.polikekgame

import com.ithersta.polikekgame.entities.GameConfig
import com.ithersta.polikekgame.entities.GameIdentifier
import com.ithersta.polikekgame.entities.GameState
import com.ithersta.polikekgame.repository.GameStateRepository
import com.ithersta.polikekgame.repository.IdentityRepository
import mu.KotlinLogging

class GameService(
    private val stateRepository: GameStateRepository,
    private val identityRepository: IdentityRepository,
    private val telegramBot: TelegramBot,
    private val config: GameConfig
) {
    private val logger = KotlinLogging.logger { }

    fun get(gameIdentifier: GameIdentifier): GameState? {
        return stateRepository.get(gameIdentifier)
    }

    fun start(gameIdentifier: GameIdentifier): GameState {
        return GameState.fromConfig(config).also {
            stateRepository.set(gameIdentifier, it)
        }
    }

    fun buy(gameIdentifier: GameIdentifier): GameState? {
        return modifyState(gameIdentifier) { it?.afterPurchase() }
    }

    fun sell(gameIdentifier: GameIdentifier): GameState? {
        return modifyState(gameIdentifier) { it?.afterSale() }
    }

    private fun modifyState(gameIdentifier: GameIdentifier, transform: (GameState?) -> GameState?): GameState? {
        val state = transform(stateRepository.get(gameIdentifier))
        stateRepository.set(gameIdentifier, state)
        if (state?.isDead == true) {
            val score = state.stats.purchases
            logger.info { "${identityRepository.get(gameIdentifier.userId)} scored $score" }
            telegramBot.setGameScore(gameIdentifier, score)
        }
        return state
    }
}
