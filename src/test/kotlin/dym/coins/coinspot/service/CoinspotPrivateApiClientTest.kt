package dym.coins.coinspot.service

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import kotlin.test.Ignore

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPrivateApiClientTest {
    private val coinspotPrivateApiClient: CoinspotPrivateApiClient by lazy {
        CoinspotPrivateApiClient(System.getenv("API_KEY"), System.getenv("API_SECRET"))
    }

    @Test
    @Ignore
    fun loadOperationsAsyncTest() {
        coinspotPrivateApiClient
            .loadOperationsAsync("2017-01-01", "2023-09-17", 500).thenAccept { operations ->
                assertNotNull(operations)

                println("buy orders ${operations.buyorders.size}")
                println("sell orders ${operations.sellorders.size}")
                println(operations)
            }.join()
    }

    @Test
    @Ignore
    fun loadTransfersHistoryAsyncTest() {
        coinspotPrivateApiClient
            .loadTransfersAsync("2017-01-01", "2023-09-17").thenAccept { transfers ->
                assertNotNull(transfers)

                println("receive ${transfers.receivetransactions.size}")
                println("send ${transfers.sendtransactions.size}")
                println(transfers)
            }.join()
    }
}