package com.ithersta.polikekgame.entities

import Rarity
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class GameConfig(
    @Contextual val initialBalance: BigInteger,
    @Contextual val baseBuyPrice: BigDecimal,
    @Contextual val initialInflationRate: BigDecimal,
    @Contextual val inflationRateIncrease: BigDecimal,
    @Contextual val initialHype: BigDecimal,
    @Contextual val hypeSellFactor: BigDecimal,
    @Contextual val hypeBuyFactor: BigDecimal,
    @Contextual val maxHype: BigDecimal,
    @Contextual val minHype: BigDecimal,
    val probabilities: Map<Rarity, Double>,
    val cards: List<Card>
) {
    @Transient
    val cardsByRarity = cards.groupBy { it.rarity }
}
