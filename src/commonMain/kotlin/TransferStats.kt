import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class TransferStats(
    val purchases: Int,
    val sales: Int,
    @Contextual val earned: BigInteger,
    @Contextual val spent: BigInteger,
    val cardsSeenByRarity: Map<Rarity, Pair<Int, Int>>
)
