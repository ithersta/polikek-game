package com.ithersta.polikekgame

import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    telegramBot.start()
    embeddedServer(
        Netty,
        port = System.getenv("PORT").toInt(),
        host = System.getenv("HOSTNAME").toString()
    ) {
        configureSerialization()
        configureAuthentication()
        configureRouting()
    }.start(wait = true)
}

