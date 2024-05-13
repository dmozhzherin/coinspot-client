package dym.coins.coinspot.service

import dym.coins.coinspot.api.request.HMACRequest
import dym.coins.coinspot.api.request.OrderHistoryRequest
import dym.coins.coinspot.api.request.TransfersHistoryRequest
import dym.coins.coinspot.api.resource.BalanceResponse
import dym.coins.coinspot.api.resource.BalancesResponse
import dym.coins.coinspot.api.resource.OrderHistoryResponse
import dym.coins.coinspot.api.resource.TransfersHistoryResponse
import java.net.URL


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

    suspend fun loadOperations(
        startDate: String,
        endDate: String,
        limit: Int? = null
    ): OrderHistoryResponse {
        val body = OrderHistoryRequest(null, null, startDate, endDate, limit)

        return callApi(URL(apiUrl + ORDER_HISTORY), body, OrderHistoryResponse::class.java)
    }

    suspend fun loadTransfers(
        startDate: String,
        endDate: String,
    ): TransfersHistoryResponse {
        val body = TransfersHistoryRequest(startDate, endDate)
        return callApi(URL(apiUrl + TRANSFER_HISTORY), body, TransfersHistoryResponse::class.java)
    }

    /**
     * Load all balances. API does not return available balances.
     * To obtain available balances use [loadBalance]
     */
    suspend fun loadBalances(): BalancesResponse =
        callApi(URL(apiUrl + BALANCES), HMACRequest.noinput(), BalancesResponse::class.java)


    suspend fun loadBalance(coin: String): BalanceResponse =
        callApi(URL("$apiUrl$BALANCE/$coin?available=yes"), HMACRequest.noinput(), BalanceResponse::class.java)

    companion object {
        private const val COINSPOT_API_V_2 = "https://www.coinspot.com.au/api/v2"
        private const val ORDER_HISTORY = "/ro/my/orders/completed"
        private const val TRANSFER_HISTORY = "/ro/my/sendreceive"
        private const val BALANCES = "/ro/my/balances"
        private const val BALANCE = "/ro/my/balance"
    }
}