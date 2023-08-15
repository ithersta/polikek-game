import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.launch
import org.w3c.dom.url.URLSearchParams
import react.ElementType
import react.create
import react.createElement
import react.dom.client.createRoot
import ui.Application
import ui.coroutineScope

fun main() {
    val container = document.createElement("div")
    document.body!!.appendChild(container)

    val query = window.location.search
    val token = URLSearchParams(query).get("token") ?: ""
    val client = GameClient(token)

    coroutineScope.launch {
        val startState = try {
            client.get()
        } catch (e: Exception) {
            null
        }
        val application = Application.create {
            this.client = client
            this.startState = startState
        }
        val root = createRoot(container as web.dom.Element)
        root.render(application)
    }
}
