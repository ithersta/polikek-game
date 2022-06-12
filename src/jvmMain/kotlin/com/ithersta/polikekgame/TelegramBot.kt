package com.ithersta.polikekgame

import com.elbekD.bot.Bot
import com.elbekD.bot.types.InlineQueryResultGame
import com.ithersta.polikekgame.entities.GameIdentifier
import com.ithersta.polikekgame.entities.Identity
import com.ithersta.polikekgame.repository.IdentityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KotlinLogging

class TelegramBot(private val identityRepository: IdentityRepository) {
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Default)
    private val logger = KotlinLogging.logger { }

    private val bot = Bot.createPolling(
        token = System.getenv("TELEGRAM_TOKEN").toString(),
        username = System.getenv("TELEGRAM_USERNAME").toString()
    ).also { bot ->
        val hostname = System.getenv("PUBLIC_HOSTNAME").toString()
        bot.onMessage {
            bot.sendGame(it.chat.id, "polikek")
        }
        bot.onCallbackQuery {
            val identity = Identity(it.from.first_name, it.from.last_name, it.from.username)
            logger.info { "$identity obtained token" }
            identityRepository.set(it.from.id, identity)
            if (it.game_short_name != "polikek") return@onCallbackQuery
            bot.answerCallbackQuery(it.id, url = "$hostname/?token=${it.createToken()}")
        }
        bot.onInlineQuery {
            bot.answerInlineQuery(it.id, listOf(InlineQueryResultGame("polikek", "polikek")))
        }
    }

    fun start() {
        bot.start()
    }

    fun setGameScore(gameIdentifier: GameIdentifier, score: Int) {
        coroutineScope.launch {
            bot.setGameScore(
                userId = gameIdentifier.userId,
                score = score,
                messageId = gameIdentifier.messageId,
                chatId = gameIdentifier.chatId,
                inlineMessageId = gameIdentifier.inlineMessageId
            )
        }
    }
}
