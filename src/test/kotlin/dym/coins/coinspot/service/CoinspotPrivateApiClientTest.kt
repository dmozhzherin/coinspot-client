package dym.coins.coinspot.service

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPrivateApiClientTest {

    @Test
    fun loadOperationsAsync() {
        CoinspotPrivateApiClient(System.getenv("API_KEY"), System.getenv("API_SECRET"))
            .loadOperationsAsync("2017-01-01", "2023-09-17", 500).thenAccept { operations ->
                assertNotNull(operations)

                println("buy orders ${operations.buyorders.size}")
                println("sell orders ${operations.sellorders.size}")
                println(operations)
            }.join();
    }
}