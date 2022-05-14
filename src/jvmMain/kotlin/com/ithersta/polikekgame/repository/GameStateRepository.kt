package com.ithersta.polikekgame.repository

import com.ithersta.polikekgame.entities.GameIdentifier
import com.ithersta.polikekgame.entities.GameState

interface GameStateRepository {
    fun get(gameIdentifier: GameIdentifier): GameState?
    fun set(gameIdentifier: GameIdentifier, gameState: GameState?)
}
