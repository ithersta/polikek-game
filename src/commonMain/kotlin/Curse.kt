import kotlinx.serialization.Serializable

@Serializable
enum class Curse(val canBeSold: Boolean = true) {
    LevashovHeight, Chinese, Halving(canBeSold = false)
}
