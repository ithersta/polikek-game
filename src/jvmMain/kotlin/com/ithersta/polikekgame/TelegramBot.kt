package com.ithersta.polikekgame

import com.elbekd.bot.Bot
import com.elbekd.bot.model.ChatId
import com.elbekd.bot.types.InlineQueryResultGame
import com.ithersta.polikekgame.entities.GameIdentifier
import com.ithersta.polikekgame.entities.Identity
import com.ithersta.polikekgame.repository.IdentityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import mu.KotlinLogging
import java.io.File

class TelegramBot(private val identityRepository: IdentityRepository) {
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Default)
    private val logger = KotlinLogging.logger { }

    private val bot = Bot.createPolling(
        token = File(System.getenv("TELEGRAM_TOKEN_FILE")).readText().trim(),
    ).also { bot ->
        val hostname = System.getenv("PUBLIC_HOSTNAME")
        bot.onMessage {
            bot.sendGame(it.chat.id, "polikek")
        }
        bot.onCallbackQuery {
            val identity = Identity(it.from.first_name, it.from.lastName, it.from.username)
            logger.info { "$identity obtained token" }
            identityRepository.set(it.from.id, identity)
            if (it.gameShortName != "polikek") return@onCallbackQuery
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
                score = score.toLong(),
                messageId = gameIdentifier.messageId,
                chatId = ChatId.IntegerId(gameIdentifier.chatId!!),
                inlineMessageId = gameIdentifier.inlineMessageId
            )
        }
    }
}
