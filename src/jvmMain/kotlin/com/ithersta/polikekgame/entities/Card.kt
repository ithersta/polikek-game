package com.ithersta.polikekgame.entities

import Rarity
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class Card(
    val displayName: String,
    val url: String,
    @Contextual val basePrice: BigDecimal,
    val rarity: Rarity
)
