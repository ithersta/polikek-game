package com.ithersta.polikekgame

data class GameIdentifier(
    val userId: Long,
    val messageId: Long?,
    val chatId: Long?,
    val inlineMessageId: String?
)
