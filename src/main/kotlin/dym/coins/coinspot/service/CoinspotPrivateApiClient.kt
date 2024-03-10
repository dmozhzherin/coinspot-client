package dym.coins.coinspot.service

import dym.coins.coinspot.api.request.HMACRequest
import dym.coins.coinspot.api.request.OrderHistoryRequest
import dym.coins.coinspot.api.request.TransfersHistoryRequest
import dym.coins.coinspot.api.resource.BalanceResponse
import dym.coins.coinspot.api.resource.BalancesResponse
import dym.coins.coinspot.api.resource.OrderHistoryResponse
import dym.coins.coinspot.api.resource.TransfersHistoryResponse
import java.net.URI
import java.util.concurrent.CompletableFuture


private const val COINSPOT_API_V_2 = "https://www.coinspot.com.au/api/v2"
private const val ORDER_HISTORY = "/ro/my/orders/completed"
private const val TRANSFER_HISTORY = "/ro/my/sendreceive"
private const val BALANCES = "/ro/my/balances"
private const val BALANCE = "/ro/my/balance"

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

    fun loadOperationsAsync(
        startDate: String,
        endDate: String,
        limit: Int? = null
    ): CompletableFuture<OrderHistoryResponse> {
        val body = OrderHistoryRequest(null, null, startDate, endDate, limit)
        val request = prepareRequest(URI.create(apiUrl + ORDER_HISTORY), body)

        return callApiAsync(request, OrderHistoryResponse::class.java)
    }

    fun loadTransfersAsync(
        startDate: String,
        endDate: String,
    ): CompletableFuture<TransfersHistoryResponse> {
        val body = TransfersHistoryRequest(startDate, endDate)
        val request = prepareRequest(URI.create(apiUrl + TRANSFER_HISTORY), body)

        return callApiAsync(request, TransfersHistoryResponse::class.java)
    }

    /**
     * Load all balances. API does not return available balances.
     * To obtain available balances use [loadBalanceAsync]
     */
    fun loadBalancesAsync(): CompletableFuture<BalancesResponse> {
        val request = prepareRequest(URI.create(apiUrl + BALANCES), HMACRequest.noinput())

        return callApiAsync(request, BalancesResponse::class.java)
    }

    fun loadBalanceAsync(coin: String): CompletableFuture<BalanceResponse> {
        val request = prepareRequest(URI.create("$apiUrl$BALANCE/$coin?available=yes"), HMACRequest.noinput())

        return callApiAsync(request, BalanceResponse::class.java)
    }
}