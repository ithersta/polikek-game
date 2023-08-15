package ui

import GameClient
import TransferGameState
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
import web.cssom.*

external interface StartScreenProps : Props {
    var setState: StateSetter<TransferGameState?>
    var client: GameClient
}

val StartScreen = FC<StartScreenProps> { props ->
    var isLoading by useState(false)
    Box {
        sx {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
            justifyContent = JustifyContent.center
            height = 100.vh
            padding = 16.px
        }
        Typography {
            sx { margin = 8.px }
            variant = TypographyVariant.h4
            +"Поликек"
        }
        Typography {
            sx { margin = 8.px }
            +"Правила игры просты. Нужно эээээээээээ"
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
            +"Начать"
        }
    }
}
