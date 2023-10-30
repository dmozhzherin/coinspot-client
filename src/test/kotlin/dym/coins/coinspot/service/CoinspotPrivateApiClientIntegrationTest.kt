package dym.coins.coinspot.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.stream.Stream
import kotlin.test.Ignore

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotPrivateApiClientIntegrationTest {
    private val coinspotPrivateApiClient: CoinspotPrivateApiClient by lazy {
        CoinspotPrivateApiClient(System.getenv("API_KEY"), System.getenv("API_SECRET"))
    }

    private val jsonMapper: ObjectMapper by lazy {
        val mapper = JsonMapper.builder()
            .addModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build()
        mapper.registerKotlinModule()
        mapper
    }


    @Test
    @Ignore
    fun loadOperationsAsyncTest() {
        coinspotPrivateApiClient
            .loadOperationsAsync("2017-01-01", LocalDate.now().toString(), 500).thenAccept { operations ->
                assertNotNull(operations)

                println("buy orders ${operations.buyorders.size}")
                println("sell orders ${operations.sellorders.size}")
                println(jsonMapper.writeValueAsString(operations))

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
    @Ignore
    fun loadTransfersHistoryAsyncTest() {
        coinspotPrivateApiClient
            .loadTransfersAsync("2017-01-01", LocalDate.now().toString()).thenAccept { transfers ->
                assertNotNull(transfers)

                println("receive ${transfers.receivetransactions.size}")
                println("send ${transfers.sendtransactions.size}")
                println(jsonMapper.writeValueAsString(transfers))
            }.join()
    }

    @Test
    @Ignore
    fun loadBalancesAsyncTest() {
        coinspotPrivateApiClient
            .loadBalancesAsync().thenAccept { balances ->
                assertNotNull(balances)

                println("balances ${balances.balances.size}")
                println(jsonMapper.writeValueAsString(balances))
            }.join()
    }
}