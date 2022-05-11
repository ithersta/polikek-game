package com.ithersta.polikekgame

import com.ithersta.polikekgame.entities.GameConfig
import com.ithersta.polikekgame.entities.toTransferGameState
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

fun HTML.index() {
    head {
        meta("viewport", "initial-scale=1, width=device-width")
        title("Поликек")
        styleLink("https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap")
        styleLink("/static/styles.css")
    }
    body {
        div {
            id = "root"
        }
        script(src = "/static/polikek-game.js") {}
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun Application.configureRouting() {
    val resource = javaClass.getResourceAsStream("/gameconfig.json") ?: return
    val gameConfig = JsonSerializer.decodeFromStream<GameConfig>(resource)
    routing {
        static("/static") {
            staticBasePackage = "static"
            resources()
        }
        get("/") {
            call.respondHtml(HttpStatusCode.OK, HTML::index)
        }
        with(gameConfig) {
            gameApi()
        }
    }
}

context(GameConfig)
        private fun Routing.gameApi() {
    val service = GameService(InMemoryGameStateRepository())
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
