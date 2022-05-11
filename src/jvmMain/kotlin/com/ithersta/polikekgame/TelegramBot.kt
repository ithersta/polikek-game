package com.ithersta.polikekgame

import com.elbekD.bot.Bot
import com.elbekD.bot.types.InlineQueryResultGame
import java.util.concurrent.ConcurrentHashMap

data class Identity(
    val firstName: String,
    val lastName: String?,
    val username: String?
) {
    override fun toString(): String {
        return "$username ($firstName $lastName)"
    }
}

val identities = ConcurrentHashMap<Long, Identity>()

val telegramBot = run {
    val bot = Bot.createPolling(
        token = System.getenv("TELEGRAM_TOKEN").toString(),
        username = "polikek_game_bot"
    )
    val hostname = System.getenv("PUBLIC_HOSTNAME").toString()
    bot.onMessage {
        bot.sendGame(it.chat.id, "polikek")
    }
    bot.onCallbackQuery {
        identities[it.from.id] = Identity(it.from.first_name, it.from.last_name, it.from.username)
        if (it.game_short_name != "polikek") return@onCallbackQuery
        bot.answerCallbackQuery(it.id, url = "$hostname/?token=${it.createToken()}")
    }
    bot.onInlineQuery {
        bot.answerInlineQuery(it.id, listOf(InlineQueryResultGame("polikek", "polikek")))
    }
    bot
}
