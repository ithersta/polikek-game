import androidx.compose.ui.window.Window
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.CanvasRect
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.url.URLSearchParams
import ui.App
import ui.ScreenModel

val coroutineScope = CoroutineScope(Dispatchers.Default)

fun main() {
    val query = window.location.search
    val token = URLSearchParams(query).get("token") ?: ""
    val client = GameClient(token)
    val screenModel = ScreenModel(client)
    coroutineScope.launch {
        screenModel.fetch()
        val composeTarget = document.getElementById("ComposeTarget") as HTMLCanvasElement
        composeTarget.width = window.innerWidth
        composeTarget.height = window.innerHeight

        onWasmReady {
            Window("Поликек") {
                App(screenModel)
            }
        }
    }
}
