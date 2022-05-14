package com.ithersta.polikekgame.entities

data class GameIdentifier(
    val userId: Long,
    val messageId: Long?,
    val chatId: Long?,
    val inlineMessageId: String?
)
