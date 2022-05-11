package com.ithersta.polikekgame

import com.ithersta.polikekgame.entities.GameState

interface GameStateRepository {
    fun get(gameIdentifier: GameIdentifier): GameState?
    fun set(gameIdentifier: GameIdentifier, gameState: GameState)
}
