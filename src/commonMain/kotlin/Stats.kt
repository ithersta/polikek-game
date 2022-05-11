import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Stats(
    val purchases: Int = 0,
    val sales: Int = 0,
    @Contextual val earned: BigInteger = BigInteger.ZERO,
    @Contextual val spent: BigInteger = BigInteger.ZERO
) {
    fun withPurchase(price: BigInteger) = copy(
        purchases = purchases + 1,
        spent = spent + price
    )

    fun withSale(price: BigInteger) = copy(
        sales = sales + 1,
        earned = earned + price
    )
}
