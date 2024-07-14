package dym.coins.coinspot.client

import dym.coins.coinspot.api.dto.OrderHistory
import dym.coins.coinspot.api.dto.TransfersHistory
import dym.coins.coinspot.api.request.HMACRequest
import dym.coins.coinspot.api.request.OrderHistoryRequest
import dym.coins.coinspot.api.request.TransfersHistoryRequest
import dym.coins.coinspot.api.resource.Balance
import dym.coins.coinspot.api.resource.BalanceResponse
import dym.coins.coinspot.api.resource.BalancesResponse
import dym.coins.coinspot.api.resource.OrderHistoryResponse
import dym.coins.coinspot.api.resource.TransfersHistoryResponse
import dym.coins.coinspot.domain.AssetType
import java.net.URL
import java.time.LocalDate


/**
 * Coinspot read-only API client
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotROApiClient
@JvmOverloads constructor(
    apiKey: String,
    apiSecret: String,
    private val apiUrl: String = COINSPOT_RO_API_V_2,
) : PrivateAPIClient(apiKey, apiSecret) {

    suspend fun loadOperations(
        startDate: LocalDate,
        endDate: LocalDate,
        limit: Int? = null
    ): OrderHistory = callApi(
        URL(apiUrl + ORDER_HISTORY),
        OrderHistoryRequest(null, null, startDate.toString(), endDate.toString(), limit),
        OrderHistoryResponse::class.java
    ) {
        OrderHistory(it.buyorders, it.sellorders)
    }

    suspend fun loadTransfers(
        startDate: LocalDate,
        endDate: LocalDate
    ): TransfersHistory = callApi(
        URL(apiUrl + TRANSFER_HISTORY),
        TransfersHistoryRequest(startDate.toString(), endDate.toString()),
        TransfersHistoryResponse::class.java
    ) {
        TransfersHistory(it.sendtransactions, it.receivetransactions)
    }

    /**
     * Load all balances API does not return available balances (some coins might be blocked for orders).
     * To obtain available balances use [loadBalance]
     */
    suspend fun loadBalances(): Map<AssetType, Balance> =
        callApi(URL(apiUrl + BALANCES), HMACRequest.noinput(), BalancesResponse::class.java) {
            it.balances
        }

    suspend fun loadBalance(coin: AssetType): Balance =
        callApi(URL("$apiUrl$BALANCE/${coin.code}?available=yes"), HMACRequest.noinput(), BalanceResponse::class.java) {
            it.balance
        }


    companion object {
        private const val COINSPOT_RO_API_V_2 = "https://www.coinspot.com.au/api/v2/ro"
        private const val ORDER_HISTORY = "/my/orders/completed"
        private const val TRANSFER_HISTORY = "/my/sendreceive"
        private const val BALANCES = "/my/balances"
        private const val BALANCE = "/my/balance"
    }
}