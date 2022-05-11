package ui

import com.ionspin.kotlin.bignum.integer.BigInteger

fun BigInteger.formatMoney(): String {
    return toString()
        .reversed()
        .chunked(3)
        .reversed()
        .joinToString(separator = " ") { it.reversed() }
}
