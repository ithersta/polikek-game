package com.ithersta.polikekgame.configuration

import com.ionspin.kotlin.bignum.serialization.kotlinx.humanReadableSerializerModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json

val JsonSerializer = Json {
    serializersModule = humanReadableSerializerModule
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(JsonSerializer)
    }
}
