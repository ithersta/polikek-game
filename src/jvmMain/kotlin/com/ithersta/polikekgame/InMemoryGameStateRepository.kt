package com.ithersta.polikekgame

import com.ithersta.polikekgame.entities.GameState
import java.util.concurrent.ConcurrentHashMap

class InMemoryGameStateRepository : GameStateRepository {
    private val states = ConcurrentHashMap<GameIdentifier, GameState>()

    override fun get(gameIdentifier: GameIdentifier): GameState? {
        return states[gameIdentifier]
    }

    override fun set(gameIdentifier: GameIdentifier, gameState: GameState) {
        states[gameIdentifier] = gameState
    }
}
