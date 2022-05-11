package ui

import GameClient
import TransferGameState
import csstype.*
import kotlinx.coroutines.launch
import mui.lab.LoadingButton
import mui.material.Box
import mui.material.ButtonColor
import mui.material.ButtonVariant
import mui.material.Typography
import mui.material.styles.TypographyVariant
import mui.system.sx
import react.FC
import react.Props
import react.StateSetter
import react.useState

external interface LoseScreenProps : Props {
    var state: TransferGameState
    var setState: StateSetter<TransferGameState?>
    var client: GameClient
}

val LoseScreen = FC<LoseScreenProps> { props ->
    var isLoading by useState(false)
    Box {
        sx {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
            justifyContent = JustifyContent.center
            flexGrow = number(1.0)
            width = 100.pct
            padding = 16.px
        }
        Typography {
            sx { margin = 8.px }
            variant = TypographyVariant.h4
            +"Вы проиграли :("
        }
        Typography {
            sx { margin = 8.px }
            +"Продержались карточек: ${props.state.stats.purchases}"
        }
        LoadingButton {
            sx {
                margin = 48.px
            }
            loading = isLoading
            variant = ButtonVariant.contained
            disableElevation = true
            color = ButtonColor.primary
            fullWidth = true
            onClick = {
                isLoading = true
                coroutineScope.launch {
                    val newState = try {
                        props.client.start()
                    } catch (e: Exception) {
                        null
                    }
                    props.setState(newState)
                    isLoading = false
                }
            }
            +"Начать заново"
        }
    }
}
