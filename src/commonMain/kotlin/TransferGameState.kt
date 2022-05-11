import com.ionspin.kotlin.bignum.integer.BigInteger
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class TransferGameState(
    @Contextual val balance: BigInteger,
    val card: TransferCard,
    val isSold: Boolean,
    @Contextual val salePrice: BigInteger,
    @Contextual val purchasePrice: BigInteger,
    val isDead: Boolean,
    val stats: Stats
)

@Serializable
data class TransferCard(
    val displayName: String,
    val url: String,
    val rarity: Rarity
)

enum class Rarity {
    Common, Uncommon, Rare, SuperRare
}
