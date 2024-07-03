package dym.coins.coinspot.client

import dym.coins.coinspot.api.resource.RatesResponse
import dym.coins.coinspot.domain.AssetType
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.RoundingMode

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPubApiClientIntegrationTest {

    @Test
    fun latestRatesAsync() {
        runBlocking {
            CoinspotPubApiClient().latestRates().apply {
                assertNotNull(this)
                assertTrue(isNotEmpty())

                println(this)
            }
        }
    }

    @Test
    fun compareRates() {
        runBlocking {
            CoinspotPubApiClient().latestRates().apply {
                val filtered = mutableMapOf<AssetType, RatesResponse.Rate>()
                entries.forEach { (key, value) ->
                    ((value.ask - value.bid) / value.bid)
                        .times("100".toBigDecimal()).setScale(2, RoundingMode.HALF_UP)
                        .takeIf { it < "10".toBigDecimal() }
                        ?.let {
                            println("$key: \t ${value.bid} \t ${value.ask} \t ${value.last} \t $it")
                            filtered[key] = with(value) {
                                copy(
                                    bid = bid.setScale(8, RoundingMode.HALF_EVEN),
                                    ask = ask.setScale(8, RoundingMode.HALF_EVEN),
                                    last = last.setScale(8, RoundingMode.HALF_EVEN)
                                )
                            }
                        }
                }

                println()
                filtered.forEach { (key, v) ->
                    print("$key ")
                    filtered.filter { it.key != key }
                        .forEach { (key2, v2) ->
                            //print exchange rates for all pairs
                            print("\t ${v.bid / v2.ask} $key2")
                        }
                    println()
                }
            }
        }
    }
}