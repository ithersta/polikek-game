import kotlinx.serialization.Serializable

@Serializable
data class TransferCard(
    val displayName: String,
    val url: String,
    val rarity: Rarity,
    val imageFit: ImageFit,
    val curses: Collection<Curse>
)
