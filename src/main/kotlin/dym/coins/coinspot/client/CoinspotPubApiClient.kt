package dym.coins.coinspot.client

import dym.coins.coinspot.api.resource.RatesResponse
import dym.coins.coinspot.domain.AssetType
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import java.net.URL

/**
 * @author dym
 * Date: 12.02.2023
 */
class CoinspotPubApiClient @JvmOverloads constructor(private val apiUrl: String = COINSPOT_PUBAPI_V_2) : APIClient() {
    private val httpClient: HttpClient = HttpClient(CIO)

    /**
     * Fetches the latest rates from the Coinspot API.
     * @return a map of asset types and their respective rates.
     * May return a {@link RatesResponse.Rate#FAULT} in the map if the rate is not parsable.
     * @throws {@link dym.coins.coinspot.exception.CoinspotException} if the API returns an error.
     */
    suspend fun latestRates(): Map<AssetType, RatesResponse.Rate> =
        httpClient.get(URL(apiUrl + LATEST_RATES)).run {
            processResponse(this, RatesResponse::class.java) { it.prices }
        }

    companion object {
        private const val COINSPOT_PUBAPI_V_2 = "https://www.coinspot.com.au/pubapi/v2"
        private const val LATEST_RATES = "/latest"
    }
}
