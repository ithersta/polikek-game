package com.ithersta.polikekgame

import com.ithersta.polikekgame.configuration.configureAuthentication
import com.ithersta.polikekgame.configuration.configureRouting
import com.ithersta.polikekgame.configuration.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.GlobalContext
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("PORT").toInt(),
        host = System.getenv("HOSTNAME")
    ) {
        if (GlobalContext.getOrNull() == null) {
            install(Koin) {
                slf4jLogger()
                modules(polikekGameModule)
            }
        }
        configureSerialization()
        configureAuthentication()
        configureRouting()
        get<TelegramBot>().start()
    }.start(wait = true)
}

