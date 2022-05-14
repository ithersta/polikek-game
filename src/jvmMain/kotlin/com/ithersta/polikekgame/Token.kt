package com.ithersta.polikekgame

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
import com.elbekD.bot.types.CallbackQuery
import com.ithersta.polikekgame.entities.GameIdentifier
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.util.pipeline.*

private const val USER_ID = "user_id"
private const val MESSAGE_ID = "message_id"
private const val CHAT_ID = "chat_id"
private const val INLINE_MESSAGE_ID = "inline_message_id"

private val secret = System.getenv("JWT_SECRET").toString()
private val algorithm = Algorithm.HMAC256(secret)

val JwtVerifier: JWTVerifier = JWT.require(algorithm).build()

val PipelineContext<Unit, ApplicationCall>.jwtGameIdentifier: GameIdentifier
    get() {
        val payload = call.principal<JWTPrincipal>()!!.payload
        return GameIdentifier(
            userId = payload.getClaim(USER_ID).asLong(),
            messageId = payload.getClaim(MESSAGE_ID).asLongOrNull(),
            chatId = payload.getClaim(CHAT_ID).asLongOrNull(),
            inlineMessageId = payload.getClaim(INLINE_MESSAGE_ID).asStringOrNull()
        )
    }

fun CallbackQuery.createToken(): String {
    return JWT.create()
        .withClaim(USER_ID, from.id)
        .withClaim(CHAT_ID, message?.chat?.id)
        .withClaim(MESSAGE_ID, message?.message_id)
        .withClaim(INLINE_MESSAGE_ID, inline_message_id)
        .sign(algorithm)
}

private fun Claim.asLongOrNull(): Long? {
    return if (isNull) {
        null
    } else {
        asLong()
    }
}

private fun Claim.asStringOrNull(): String? {
    return if (isNull) {
        null
    } else {
        asString()
    }
}
