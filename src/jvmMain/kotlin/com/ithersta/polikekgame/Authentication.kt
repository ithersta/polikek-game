package com.ithersta.polikekgame

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("jwt") {
            verifier(JwtVerifier)
            validate { JWTPrincipal(it.payload) }
        }
    }
}
