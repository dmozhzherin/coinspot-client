package dym.coins.coinspot.client

import dym.coins.coinspot.api.request.HMACRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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

    private val lock = Mutex()

    private val keySpec = SecretKeySpec(apiSecret.toByteArray(), HMAC_SHA_512)

    private fun genSign(message: ByteArray): ByteArray =
        Mac.getInstance(HMAC_SHA_512).run {
            init(keySpec)
            doFinal(message)
        }

    private fun <T : HMACRequest> HttpRequestBuilder.prepareRequest(body: T) {
        val message = objectWriter.writeValueAsBytes(body.nonced())
        val sign = HexFormat.of().formatHex(genSign(message))
            method = HttpMethod.Post
            setBody(message)
            headers {
                append("Content-Type", "application/json")
                append("key", apiKey)
                append("sign", sign)
            }
    }

    /**
     * Prepare and run the request.
     * The call is synchronized in an attempt to ensure the correct order of the ever-growing nonce
     */
    protected suspend fun <T : HMACRequest, P, R> callApi(
        uri: String,
        body: T,
        clazz: Class<P>,
        transform: (P) -> R = { it as R }
    ): R = lock.withLock {
        httpClient.post(uri){ prepareRequest(body) }.run {
            processResponse(this, clazz, transform)
        }
    }

}
