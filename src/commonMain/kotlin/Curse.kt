import kotlinx.serialization.Serializable

@Serializable
enum class Curse(val canBeSold: Boolean = true) {
    LevashovHeight,
    Chinese,
    Jordan,
    Halving(canBeSold = false),
    Doubling(canBeSold = false),
}
