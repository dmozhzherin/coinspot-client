package dym.coins.coinspot.service

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPubApiClientIntegrationTest {

    @Test
    @Ignore
    fun loadPricesAsync() {
        CoinspotPubApiClient().loadPricesAsync().thenAccept { rates ->
            assertNotNull(rates)
            assertTrue(rates.isNotEmpty())

            println(rates)
        }.join();
    }
}