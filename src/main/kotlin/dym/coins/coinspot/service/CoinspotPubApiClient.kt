package dym.coins.coinspot.service

import dym.coins.coinspot.api.resource.RatesResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.concurrent.CompletableFuture

/**
 * @author dym
 * Date: 12.02.2023
 */
class CoinspotPubApiClient @JvmOverloads constructor(private val apiUrl: String = COINSPOT_PUBAPI_V_2) : APIClient() {
    private val httpClient: HttpClient = HttpClient.newHttpClient()

    fun loadPricesAsync(): CompletableFuture<Map<String, RatesResponse.Rate>> {
        val request = HttpRequest.newBuilder(URI.create(apiUrl + LATEST_RATES)).GET().build()

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
            .thenApply { response ->
                processResponse(response, RatesResponse::class.java) { it: RatesResponse -> it.prices }
            }
    }

    companion object {
        private const val COINSPOT_PUBAPI_V_2 = "https://www.coinspot.com.au/pubapi/v2"
        private const val LATEST_RATES = "/latest"
    }
}
