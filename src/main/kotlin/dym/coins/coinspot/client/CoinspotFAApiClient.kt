package dym.coins.coinspot.client

import dym.coins.coinspot.api.dto.SwapResult
import dym.coins.coinspot.api.request.SwapNowRequest
import dym.coins.coinspot.api.request.SwapQuoteRequest
import dym.coins.coinspot.api.resource.SwapNowResponse
import dym.coins.coinspot.api.resource.SwapQuoteResponse
import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal
import java.net.URL


/**
 * Coinspot full access API client
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotFAApiClient
@JvmOverloads constructor(
    apiKey: String,
    apiSecret: String,
    private val apiUrl: String = COINSPOT_FA_API_V_2,
) : PrivateAPIClient(apiKey, apiSecret) {


    suspend fun requestSwapQuote(from: AssetType, to: AssetType, amount: BigDecimal): BigDecimal =
        callApi(URL("$apiUrl$SWAP_NOW_QUOTE"), SwapQuoteRequest(from, to, amount), SwapQuoteResponse::class.java) {
            it.rate
        }

    suspend fun swapNow(
        from: AssetType,
        to: AssetType,
        amount: BigDecimal,
        rate: BigDecimal?,
        treshold: BigDecimal?
    ): SwapResult =
        callApi(
            URL("$apiUrl$SWAP_NOW"),
            SwapNowRequest(from, to, amount, rate, treshold), SwapNowResponse::class.java
        ) { result ->
            val assetTo = result.market.split("/").let { market ->
                AssetType.of(market[0]).takeIf { it != result.coin } ?: AssetType.of(market[1])
            }
            SwapResult(result.coin, assetTo, result.amount, result.rate, result.total)
        }

    companion object {
        private const val COINSPOT_FA_API_V_2 = "https://www.coinspot.com.au/api/v2"

        private const val SWAP_NOW_QUOTE = "/quote/swap/now"
        private const val SWAP_NOW = "/my/swap/now"
    }
}