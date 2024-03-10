package dym.coins.coinspot.service

import dym.coins.coinspot.api.request.HMACRequest
import dym.coins.coinspot.api.resource.ResponseMeta
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.HexFormat
import java.util.concurrent.CompletableFuture
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * @author dym
 * Date: 17.09.2023
 */

private const val HMAC_SHA_512 = "HmacSHA512"
abstract class PrivateAPIClient(private val apiKey: String, apiSecret: String) : APIClient() {

    private val keySpec = SecretKeySpec(apiSecret.toByteArray(), HMAC_SHA_512)
    private val httpClient: HttpClient = HttpClient.newHttpClient()

    private fun genSign(message: ByteArray): ByteArray {
        return Mac.getInstance(HMAC_SHA_512).run {
            init(keySpec)
            update(message)
            doFinal()
        }
    }

    protected fun <T : HMACRequest> prepareRequest(uri: URI, body: T): HttpRequest? {
        val message = objectWriter.writeValueAsBytes(body)
        val sign = HexFormat.of().formatHex(genSign(message))

        return HttpRequest.newBuilder(uri)
            .header("Content-Type", "application/json")
            .header("key", apiKey)
            .header("sign", sign)
            .POST(HttpRequest.BodyPublishers.ofByteArray(message))
            .build()
    }

    protected fun <T: ResponseMeta> callApiAsync(request: HttpRequest?, clazz: Class<T>): CompletableFuture<T> {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
            .thenApply { response ->
                processResponse(response, clazz) { it }
            }
    }

}