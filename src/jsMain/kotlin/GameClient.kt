import com.ionspin.kotlin.bignum.serialization.kotlinx.humanReadableSerializerModule
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class GameClient(token: String) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                serializersModule = humanReadableSerializerModule
            })
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(token, token)
                }
            }
        }
    }
    private val state_ = MutableStateFlow<TransferGameState?>(null)
    val state = state_.asStateFlow()

    suspend fun start() {
        state_.value = client.post("/api/new").body()
    }

    suspend fun buy() {
        state_.value = client.post("/api/buy").body()
    }

    suspend fun sell() {
        state_.value = client.post("/api/sell").body()
    }

    suspend fun get() {
        state_.value = try {
            client.get("/api/state").body()
        } catch (e: Exception) {
            null
        }
    }
}
