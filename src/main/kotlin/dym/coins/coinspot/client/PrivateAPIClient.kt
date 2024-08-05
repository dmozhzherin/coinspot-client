package dym.coins.coinspot.client

import dym.coins.coinspot.api.request.HMACRequest
import dym.coins.coinspot.api.resource.ResponseMeta
import dym.coins.coinspot.exception.CoinspotException
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.invoke
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.net.URL
import java.util.HexFormat
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * @author dym
 * Date: 17.09.2023
 */

private const val HMAC_SHA_512 = "HmacSHA512"

/**
 * All private clients are constructed with a key and a secret. That means every customer must have their own API client.
 */
abstract class PrivateAPIClient(private val apiKey: String, apiSecret: String) : APIClient() {

    private val keySpec = SecretKeySpec(apiSecret.toByteArray(), HMAC_SHA_512)
    private val httpClient: HttpClient = HttpClient(CIO)

    private fun genSign(message: ByteArray): ByteArray =
        Mac.getInstance(HMAC_SHA_512).run {
            init(keySpec)
            update(message)
            doFinal()
        }

    private fun <T : HMACRequest> prepareRequest(url: URL, body: T): HttpRequestBuilder {
        val message = objectWriter.writeValueAsBytes(body.nonced())
        val sign = HexFormat.of().formatHex(genSign(message))

        return HttpRequestBuilder(url).apply {
            method = HttpMethod.Post
            setBody(message)
            headers {
                append("Content-Type", "application/json")
                append("key", apiKey)
                append("sign", sign)
            }
        }
    }

    /**
     * Prepare and run the request.
     * The call is synchronized in an attempt to ensure the correct order of the ever-growing nonce
     */
    protected suspend fun <T : HMACRequest, P : ResponseMeta, R> callApi(
        url: URL,
        body: T,
        clazz: Class<P>,
        transform: (P) -> R = { it as R }
    ): R = lock.withLock {
        val request = prepareRequest(url, body)
        coroutineScope { async { httpClient.post(request) } }
    }.await().run {
        try {
            processResponse(this, clazz, transform)
        } catch (e: CoinspotException) {
            throw CoinspotException("API call failed. Request: ${objectWriter.writeValueAsBytes(body)}", e)
        }
    }

    //The mutex is static and that means all customers are synchronised, which is excessive. I'll think about it later.
    private companion object {
        @JvmStatic
        val lock = Mutex()
    }
}