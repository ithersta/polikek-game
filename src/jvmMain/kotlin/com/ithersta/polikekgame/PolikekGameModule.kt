@file:OptIn(ExperimentalSerializationApi::class)

package com.ithersta.polikekgame

import com.ithersta.polikekgame.configuration.JsonSerializer
import com.ithersta.polikekgame.entities.GameConfig
import com.ithersta.polikekgame.repository.GameStateRepository
import com.ithersta.polikekgame.repository.IdentityRepository
import com.ithersta.polikekgame.repository.InMemoryGameStateRepository
import com.ithersta.polikekgame.repository.InMemoryIdentityRepository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val polikekGameModule = module(createdAtStart = true) {
    single {
        val resource = javaClass.getResourceAsStream("/gameconfig.json")!!
        JsonSerializer.decodeFromStream<GameConfig>(resource)
    }
    singleOf(::InMemoryGameStateRepository) bind GameStateRepository::class
    singleOf(::InMemoryIdentityRepository) bind IdentityRepository::class
    singleOf(::TelegramBot)
    singleOf(::GameService)
}
