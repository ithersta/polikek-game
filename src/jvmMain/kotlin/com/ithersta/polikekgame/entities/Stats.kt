package com.ithersta.polikekgame.entities

import Rarity
import TransferStats
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.serialization.Contextual

data class Stats(
    val purchases: Int = 0,
    val sales: Int = 0,
    @Contextual val earned: BigInteger = BigInteger.ZERO,
    @Contextual val spent: BigInteger = BigInteger.ZERO,
    val seenCards: Set<Card> = emptySet()
) {
    fun withPurchase(price: BigInteger, card: Card) = copy(
        purchases = purchases + 1,
        spent = spent + price,
        seenCards = seenCards + card
    )

    fun withSale(price: BigInteger) = copy(
        sales = sales + 1,
        earned = earned + price
    )
}

fun Stats.toTransferStats(gameConfig: GameConfig): TransferStats {
    return TransferStats(
        purchases = purchases,
        sales = sales,
        earned = earned,
        spent = spent,
        cardsSeenByRarity = Rarity.values()
            .associateWith { rarity ->
                seenCards.count { it.rarity == rarity } to
                        gameConfig.cardsByRarity[rarity]!!.size
            }
    )
}
