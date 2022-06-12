package com.ithersta.polikekgame.entities

import Curse
import TransferGameState
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlin.random.Random

data class GameState private constructor(
    val balance: BigInteger,
    val hype: BigDecimal,
    val cumulativeInflation: BigDecimal,
    val inflationRate: BigDecimal,
    val card: Card,
    val isSold: Boolean,
    val stats: Stats,
    val config: GameConfig
) {
    companion object {
        fun fromConfig(config: GameConfig): GameState {
            val card = config.generateCard()
            return GameState(
                balance = config.initialBalance,
                card = card,
                isSold = false,
                hype = config.initialHype,
                cumulativeInflation = BigDecimal.ONE,
                inflationRate = config.initialInflationRate,
                stats = Stats().withPurchase(BigInteger.ZERO, card),
                config = config
            )
        }
    }

    val purchasePrice: BigInteger
        get() = (config.baseBuyPrice * cumulativeInflation).toBigInteger()

    val salePrice: BigInteger
        get() = (card.basePrice * cumulativeInflation * hype).toBigInteger()

    val isDead: Boolean
        get() = balance < purchasePrice && isSold

    fun afterPurchase(): GameState {
        require(!isDead)
        require(balance >= purchasePrice)
        val generatedCard = config.generateCard(not = card)
        val baseState = copy(
            balance = balance - purchasePrice,
            card = generatedCard,
            isSold = false,
            hype = run {
                val factor = if (isSold) config.hypeSellFactor else config.hypeBuyFactor
                (hype * factor).coerceIn(config.minHype, config.maxHype)
            },
            cumulativeInflation = cumulativeInflation * inflationRate,
            inflationRate = inflationRate + config.inflationRateIncrease,
            stats = stats.withPurchase(purchasePrice, generatedCard),
        )
        return generatedCard.curses.fold(baseState) { acc, curse ->
            acc.afterCurseEffect(curse)
        }
    }

    fun afterSale(): GameState {
        require(!isSold)
        require(!isDead)
        require(card.curses.all { it.canBeSold })
        return copy(
            balance = balance + salePrice,
            isSold = true,
            stats = stats.withSale(salePrice),
        )
    }

    private fun afterCurseEffect(curse: Curse): GameState = when (curse) {
        Curse.Halving -> copy(
            balance = balance / 2
        )
        Curse.Chinese -> this
        Curse.LevashovHeight -> this
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
        stats = stats.toTransferStats(config)
    )
}

