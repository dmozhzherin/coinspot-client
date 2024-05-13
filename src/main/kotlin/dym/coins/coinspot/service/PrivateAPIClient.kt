package dym.coins.coinspot.service

import dym.coins.coinspot.api.request.HMACRequest
import dym.coins.coinspot.api.resource.ResponseMeta
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.headers
import io.ktor.client.request.invoke
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import java.net.URL
import java.util.HexFormat
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * @author dym
 * Date: 17.09.2023
 */

private const val HMAC_SHA_512 = "HmacSHA512"

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
        val message = objectWriter.writeValueAsBytes(body)
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

    protected suspend fun <T : HMACRequest, R : ResponseMeta> callApi(
        url: URL,
        body: T,
        clazz: Class<R>,
    ): R =
        httpClient.post(prepareRequest(url, body)).run {
            return processResponse(this, clazz) { it }
        }

}