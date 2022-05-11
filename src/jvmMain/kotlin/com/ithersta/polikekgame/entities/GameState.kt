package com.ithersta.polikekgame.entities

import Stats
import TransferCard
import TransferGameState
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.random.Random

class GameState private constructor(
    val balance: BigInteger,
    val hype: BigDecimal,
    val cumulativeInflation: BigDecimal,
    val inflationRate: BigDecimal,
    val card: Card,
    val isSold: Boolean,
    val isDead: Boolean,
    val stats: Stats
) {
    companion object {
        context(GameConfig)
        fun fromConfig(): GameState {
            return GameState(
                balance = initialBalance,
                card = generateCard(),
                isSold = false,
                isDead = false,
                hype = initialHype,
                cumulativeInflation = BigDecimal.ONE,
                inflationRate = initialInflationRate,
                stats = Stats()
            )
        }
    }

    context(GameConfig)
    val purchasePrice: BigInteger
        get() = (baseBuyPrice * cumulativeInflation).toBigInteger()

    context(GameConfig)
    val salePrice: BigInteger
        get() = (card.basePrice * cumulativeInflation * hype).toBigInteger()

    context(GameConfig)
    fun afterPurchase(): GameState {
        require(!isDead)
        require(balance >= purchasePrice)
        return GameState(
            balance = balance - purchasePrice,
            card = generateCard(not = card),
            isSold = false,
            isDead = isDead,
            hype = run {
                val factor = if (isSold) hypeSellFactor else hypeBuyFactor
                (hype * factor).coerceIn(minHype, maxHype)
            },
            cumulativeInflation = cumulativeInflation * inflationRate,
            inflationRate = inflationRate + inflationRateIncrease,
            stats = stats.withPurchase(purchasePrice)
        )
    }

    context(GameConfig)
    fun afterSale(): GameState {
        require(!isSold)
        require(!isDead)
        return GameState(
            balance = balance + salePrice,
            card = card,
            isSold = true,
            isDead = (balance + salePrice) <= purchasePrice,
            hype = hype,
            cumulativeInflation = cumulativeInflation,
            inflationRate = inflationRate,
            stats = stats.withSale(salePrice)
        )
    }
}

context(GameConfig)
        private tailrec fun generateCard(not: Card? = null): Card {
    val random = Random.nextDouble()
    val requiredRarity = probabilities
        .asSequence()
        .filter { random <= it.value }
        .maxOf { it.key }
    val chosenCard = cardsByRarity[requiredRarity]!!.random()
    return if (chosenCard != not) {
        chosenCard
    } else {
        generateCard(not)
    }
}

context(GameConfig)
fun GameState.toTransferGameState(): TransferGameState {
    return TransferGameState(
        balance = balance,
        card = TransferCard(
            displayName = card.displayName,
            url = card.url,
            rarity = card.rarity
        ),
        isSold = isSold,
        salePrice = salePrice,
        purchasePrice = purchasePrice,
        isDead = isDead,
        stats = stats
    )
}
