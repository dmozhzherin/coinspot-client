package dym.coins.coinspot.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dym.coins.coinspot.domain.AssetType
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.Ignore

/**
 * @author dym
 * Date: 18.05.2024
 */
class CoinspotFAApiClientIntegrationTest {

    //The test fails if the API key and secret are not set in the environment variables
    //To run the test one must have an account on the coinspot.com.au
    private val coinspotFAApiClient: CoinspotFAApiClient by lazy {
        CoinspotFAApiClient(System.getenv("FA_KEY"), System.getenv("FA_SECRET"))
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
    fun testSwapQuote() = runBlocking {
        launch {
            coinspotFAApiClient
                .swapQuote(AssetType.of("DOGE"), AssetType.of("BTC"), BigDecimal("1")).apply {
                    assertNotNull(this)

                    println(this)
                }
        }.join()
    }

    @Test
    @Ignore
    fun testSellQuote() = runBlocking {
        launch {
            coinspotFAApiClient
                .sellQuote(AssetType.of("DOGE"), BigDecimal("100")).apply {
                    assertNotNull(this)

                    println(this)
                }
        }.join()
    }
}
