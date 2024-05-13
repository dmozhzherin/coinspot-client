package dym.coins.coinspot.service

import dym.coins.coinspot.api.resource.RatesResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.RoundingMode
import kotlin.test.Ignore

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPubApiClientIntegrationTest {

    @Test
    @Ignore
    fun loadPricesAsync() = runBlocking {
        launch {
            CoinspotPubApiClient().loadPrices().apply {
                assertNotNull(this)
                assertTrue(isNotEmpty())

                println(this)
            }
        }.join()
    }

    @Test
    fun compareRates() = runBlocking {
        launch {
            CoinspotPubApiClient().loadPrices().apply {
                val filtered = mutableMapOf<String, RatesResponse.Rate>()
                entries.forEach { (key, value) ->
                    ((value.ask - value.bid) / value.bid)
                        .times("100".toBigDecimal()).setScale(2, RoundingMode.HALF_UP)
                        .takeIf { it < "10".toBigDecimal() }
                        ?.let {
                            println("$key: \t ${value.bid} \t ${value.ask} \t ${value.last} \t $it")
                            filtered[key] = with(value) {
                                copy(
                                    bid = bid.setScale(8),
                                    ask = ask.setScale(8),
                                    last = last.setScale(8)
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
        }.join()
    }
}