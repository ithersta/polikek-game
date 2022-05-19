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

