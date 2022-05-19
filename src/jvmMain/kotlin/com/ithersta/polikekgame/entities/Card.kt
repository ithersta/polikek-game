package com.ithersta.polikekgame.entities

import ImageFit
import Rarity
import TransferCard
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class Card(
    val displayName: String,
    val url: String,
    @Contextual val basePrice: BigDecimal,
    val rarity: Rarity,
    val imageFit: ImageFit = ImageFit.Cover
)

fun Card.toTransferCard(): TransferCard {
    return TransferCard(
        displayName = displayName,
        url = url,
        rarity = rarity,
        imageFit = imageFit
    )
}
