package dym.coins.coinspot.service

import dym.coins.coinspot.api.request.OrderHistoryRequest
import dym.coins.coinspot.api.resource.OrderHistoryResponse
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpResponse.BodyHandlers
import java.util.concurrent.CompletableFuture


private const val COINSPOT_API_V_2 = "https://www.coinspot.com.au/api/v2"
private const val ORDER_HISTORY = "/ro/my/orders/completed"

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPrivateApiClient
@JvmOverloads constructor(
    apiKey: String,
    apiSecret: String,
    private val apiUrl: String = COINSPOT_API_V_2,
) : PrivateAPIClient(apiKey, apiSecret) {

    private val httpClient: HttpClient = HttpClient.newHttpClient()
    fun loadOperationsAsync(
        startDate: String,
        endDate: String,
        limit: Int? = null
    ): CompletableFuture<OrderHistoryResponse> {

        val body = OrderHistoryRequest(null, null, startDate, endDate, limit);
        val request = prepareRequest(URI.create(apiUrl + ORDER_HISTORY), body)

        return httpClient.sendAsync(request, BodyHandlers.ofInputStream()).thenApply { response ->
            processResponse(response, OrderHistoryResponse::class.java) { it }
        }
    }

}