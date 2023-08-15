package ui

import Rarity
import js.core.jso
import mui.material.PaletteMode
import mui.material.styles.createTheme
import web.cssom.Color
import web.cssom.px

private const val Neutral99 = "#FFFBFE"
private const val Purple40 = "#6750A4"

private const val Neutral10 = "#1C1B1F"
private const val Purple80 = "#D0BCFF"

val LightTheme = createTheme(
    jso {
        palette = jso {
            mode = PaletteMode.light
            primary = jso {
                main = Purple40
            }
            info = jso {
                main = Neutral99
            }
            background = jso {
                default = Neutral99
            }
        }
        shape = jso {
            borderRadius = 8.px
        }
    }
)

val DarkTheme = createTheme(
    jso {
        palette = jso {
            mode = PaletteMode.dark
            primary = jso {
                main = Purple80
            }
            info = jso {
                main = Neutral10
            }
            background = jso {
                default = Neutral10
            }
        }
        shape = jso {
            borderRadius = 8.px
        }
    }
)

fun Rarity.color(isDark: Boolean): Color {
    return if (isDark) darkColor() else lightColor()
}

private fun Rarity.darkColor() = when (this) {
    Rarity.Common -> Color(Neutral99)
    Rarity.Uncommon -> Color("#8fcdff")
    Rarity.Rare -> Color("#d4bbff")
    Rarity.SuperRare -> Color("#ffb1ca")
}

private fun Rarity.lightColor() = when (this) {
    Rarity.Common -> Color(Neutral10)
    Rarity.Uncommon -> Color("#006397")
    Rarity.Rare -> Color("#7604ff")
    Rarity.SuperRare -> Color("#b6007a")
}
