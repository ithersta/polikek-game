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
    val stats: Stats,
    private val config: GameConfig
) {
    companion object {
        fun fromConfig(config: GameConfig): GameState {
            return GameState(
                balance = config.initialBalance,
                card = config.generateCard(),
                isSold = false,
                isDead = false,
                hype = config.initialHype,
                cumulativeInflation = BigDecimal.ONE,
                inflationRate = config.initialInflationRate,
                stats = Stats(),
                config = config
            )
        }
    }

    val purchasePrice: BigInteger
        get() = (config.baseBuyPrice * cumulativeInflation).toBigInteger()

    val salePrice: BigInteger
        get() = (card.basePrice * cumulativeInflation * hype).toBigInteger()

    fun afterPurchase(): GameState {
        require(!isDead)
        require(balance >= purchasePrice)
        return GameState(
            balance = balance - purchasePrice,
            card = config.generateCard(not = card),
            isSold = false,
            isDead = isDead,
            hype = run {
                val factor = if (isSold) config.hypeSellFactor else config.hypeBuyFactor
                (hype * factor).coerceIn(config.minHype, config.maxHype)
            },
            cumulativeInflation = cumulativeInflation * inflationRate,
            inflationRate = inflationRate + config.inflationRateIncrease,
            stats = stats.withPurchase(purchasePrice),
            config = config
        )
    }

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
            stats = stats.withSale(salePrice),
            config = config
        )
    }
}

private tailrec fun GameConfig.generateCard(not: Card? = null): Card {
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

fun GameState.toTransferGameState(): TransferGameState {
    return TransferGameState(
        balance = balance,
        card = card.toTransferCard(),
        isSold = isSold,
        salePrice = salePrice,
        purchasePrice = purchasePrice,
        isDead = isDead,
        stats = stats
    )
}
