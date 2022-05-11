import com.ionspin.kotlin.bignum.serialization.kotlinx.humanReadableSerializerModule
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
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

    suspend fun start(): TransferGameState {
        return client.post("/api/new").body()
    }

    suspend fun buy(): TransferGameState? {
        return client.post("/api/buy").body()
    }

    suspend fun sell(): TransferGameState? {
        return client.post("/api/sell").body()
    }

    suspend fun get(): TransferGameState? {
        return client.get("/api/state").body()
    }
}
