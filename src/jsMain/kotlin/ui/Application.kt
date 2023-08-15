package ui

import GameClient
import TransferGameState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import mui.material.Container
import mui.material.CssBaseline
import mui.material.useMediaQuery
import mui.system.Breakpoint
import mui.system.ThemeProvider
import mui.system.sx
import react.FC
import react.Props
import react.useState
import web.cssom.px
import web.cssom.vh

val coroutineScope = CoroutineScope(Job() + Dispatchers.Default)

external interface ApplicationProps : Props {
    var client: GameClient
    var startState: TransferGameState?
}

val Application = FC<ApplicationProps> { props ->
    val (state, setState) = useState(props.startState)
    val prefersDarkTheme = useMediaQuery("(prefers-color-scheme: dark)")
    ThemeProvider {
        theme = if (prefersDarkTheme) DarkTheme else LightTheme
        CssBaseline()
        Container {
            sx {
                height = 100.vh
                padding = 0.px
            }
            maxWidth = Breakpoint.sm
            when (state) {
                null -> StartScreen {
                    this.setState = setState
                    this.client = props.client
                }
                else -> GameScreen {
                    this.state = state
                    this.setState = setState
                    this.client = props.client
                }
            }
        }
    }
}
