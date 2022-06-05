package com.ithersta.polikekgame.configuration

import com.ithersta.polikekgame.GameService
import com.ithersta.polikekgame.entities.GameConfig
import com.ithersta.polikekgame.entities.toTransferGameState
import com.ithersta.polikekgame.jwtGameIdentifier
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.html.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.decodeFromStream
import org.koin.ktor.ext.inject

fun HTML.index() {
    head {
        meta("viewport", "initial-scale=1, width=device-width")
        title("Поликек")
        script(src = "/static/skiko.js") {}
        styleLink("/static/styles.css")
    }
    body {
        div {
            canvas {
                id = "ComposeTarget"
                width = "100%"
                height = "100%"
            }
        }
        script(src = "/static/polikek-game.js") {}
    }
}

fun Application.configureRouting() {
    routing {
        static("/static") {
            staticBasePackage = "static"
            resources()
        }
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        gameApi()
    }
}

private fun Routing.gameApi() {
    val service by inject<GameService>()
    authenticate("jwt") {
        route("/api") {
            post("/new") {
                call.respond(service.start(jwtGameIdentifier).toTransferGameState())
            }
            post("/buy") {
                call.respond(service.buy(jwtGameIdentifier)?.toTransferGameState() ?: "null")
            }
            post("/sell") {
                call.respond(service.sell(jwtGameIdentifier)?.toTransferGameState() ?: "null")
            }
            get("/state") {
                call.respond(service.get(jwtGameIdentifier)?.toTransferGameState() ?: "null")
            }
        }
    }
}
