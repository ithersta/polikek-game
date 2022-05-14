package ui

import GameClient
import TransferGameState
import csstype.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.lab.LoadingButton
import mui.material.*
import mui.material.styles.Theme
import mui.material.styles.TypographyVariant
import mui.material.styles.useTheme
import mui.system.sx
import org.w3c.dom.Image
import react.*
import react.dom.html.ReactHTML.img
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

external interface GameScreenProps : Props {
    var state: TransferGameState
    var setState: StateSetter<TransferGameState?>
    var client: GameClient
}

val GameScreen = FC<GameScreenProps> { props ->
    var loadingNewCard by useState(false)
    var loadingSell by useState(false)
    var showError by useState(false)
    val isDarkTheme = useTheme<Theme>().palette.mode == PaletteMode.dark
    Snackbar {
        open = showError
        autoHideDuration = 6000
        onClose = { _, _ -> showError = false }
        message = ReactNode("Ошибка сети. Обновите страницу.")
    }
    Box {
        sx {
            display = Display.flex
            flexDirection = FlexDirection.column
            alignItems = AlignItems.center
            position = Position.relative
            height = 100.vh
        }
        Chip {
            sx {
                top = 16.px
                right = 16.px
                position = Position.absolute
                fontSize = 12.pt
                fontWeight = FontWeight.bolder
            }
            color = ChipColor.info
            label = ReactNode(props.state.balance.formatMoney())
        }
        if (props.state.isDead) {
            LoseScreen {
                this.state = props.state
                this.setState = props.setState
                this.client = props.client
            }
        } else {
            CardMedia {
                sx {
                    flexGrow = number(1.0)
                    objectFit = ObjectFit.cover
                }
                component = img
                image = props.state.card.url
            }
        }
        Typography {
            sx {
                marginTop = 12.px
                fontSize = 14.pt
                color = props.state.card.rarity.color(isDarkTheme)
            }
            +props.state.card.displayName
        }
        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.row
                paddingLeft = 16.px
                paddingRight = 16.px
                width = 100.pct
            }
            Typography {
                sx {
                    fontWeight = FontWeight.bolder
                    fontSize = 14.pt
                }
                variant = TypographyVariant.subtitle2
                +"−${props.state.purchasePrice.formatMoney()}"
            }
            Typography {
                sx {
                    flexGrow = number(1.0)
                }
            }
            Typography {
                sx {
                    fontWeight = FontWeight.bolder
                    fontSize = 14.pt
                }
                variant = TypographyVariant.subtitle2
                +"+${props.state.salePrice.formatMoney()}"
            }
        }
        Box {
            sx {
                display = Display.flex
                flexDirection = FlexDirection.row
                paddingLeft = 16.px
                paddingRight = 16.px
                paddingBottom = 16.px
                width = 100.pct
            }
            LoadingButton {
                sx {
                    flexGrow = number(1.0)
                    marginRight = 4.px
                    height = 48.px
                }
                variant = ButtonVariant.contained
                disableElevation = true
                color = ButtonColor.primary
                fullWidth = true
                disabled = props.state.balance < props.state.purchasePrice || loadingNewCard
                loading = loadingNewCard
                onClick = {
                    coroutineScope.launch {
                        if (loadingSell) {
                            return@launch
                        }
                        loadingNewCard = true
                        try {
                            val stateDeferred = async {
                                props.client.buy().also {
                                    if (it != null) {
                                        preloadImage(it.card.url)
                                    }
                                }
                            }
                            delay(400)
                            props.setState(stateDeferred.await())
                        } catch (e: Exception) {
                            showError = true
                        }
                        loadingNewCard = false
                    }
                }
                +"Играть"
            }
            LoadingButton {
                sx {
                    flexGrow = number(1.0)
                    marginLeft = 4.px
                    height = 48.px
                }
                variant = ButtonVariant.contained
                disableElevation = true
                color = ButtonColor.primary
                fullWidth = true
                disabled = props.state.isSold || loadingNewCard || loadingSell
                loading = loadingSell
                onClick = {
                    coroutineScope.launch {
                        loadingSell = true
                        try {
                            props.setState(props.client.sell())
                        } catch (e: Exception) {
                            showError = true
                        }
                        loadingSell = false
                    }
                }
                +"Продать"
            }
        }
    }
}

private suspend fun preloadImage(url: String) {
    suspendCoroutine<Unit> { continuation ->
        val image = Image()
        image.src = url
        image.onload = {
            continuation.resume(Unit)
        }
        image.onerror = { _, _, _, _, _ ->
            continuation.resumeWithException(Exception())
        }
    }
}
