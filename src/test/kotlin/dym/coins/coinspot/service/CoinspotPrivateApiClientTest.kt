package dym.coins.coinspot.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.stream.Stream
import kotlin.test.Ignore

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPrivateApiClientTest {
    private val coinspotPrivateApiClient: CoinspotPrivateApiClient by lazy {
        CoinspotPrivateApiClient(System.getenv("API_KEY"), System.getenv("API_SECRET"))
    }

    private val jsonMapper: ObjectMapper by lazy {
        val mapper = JsonMapper()
        mapper.registerKotlinModule()
        mapper.registerModule(JavaTimeModule())
        mapper
    }


    @Test
    @Ignore
    fun loadOperationsAsyncTest() {
        coinspotPrivateApiClient
            .loadOperationsAsync("2017-01-01", "2023-09-20", 500).thenAccept { operations ->
                assertNotNull(operations)

                println("buy orders ${operations.buyorders.size}")
                println("sell orders ${operations.sellorders.size}")
                println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(operations))

                var scale = 0
                Stream.of(operations.buyorders, operations.sellorders)
                    .flatMap { it.stream() }
                    .forEach {
                        scale = maxOf(scale, it.rate.scale())
                        scale = maxOf(scale, it.amount.scale())
                        scale = maxOf(scale, it.audGst.scale())
                        scale = maxOf(scale, it.audtotal.scale())
                        scale = maxOf(scale, it.total.scale())
                    }
                println("scale $scale")
            }.join()
    }

    @Test
//    @Ignore
    fun loadTransfersHistoryAsyncTest() {
        coinspotPrivateApiClient
            .loadTransfersAsync("2017-01-01", "2023-09-17").thenAccept { transfers ->
                assertNotNull(transfers)

                println("receive ${transfers.receivetransactions.size}")
                println("send ${transfers.sendtransactions.size}")
                println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(transfers))
            }.join()
    }
}