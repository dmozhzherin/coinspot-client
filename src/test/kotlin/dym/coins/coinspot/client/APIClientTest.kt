package dym.coins.coinspot.client

import dym.coins.coinspot.api.resource.RateResponse
import dym.coins.coinspot.domain.AssetType
import dym.coins.coinspot.exception.CoinspotApiException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import kotlin.test.Test

/**
 * @author dym
 * Date: 03.08.2024
 */
class APIClientTest {


    @Test
    fun testBigDecimalIsSerializedWithoutTrailingZeroes() {
        object : APIClient() {

            fun testBigDecimalSerialization() {
                "100.00000".toBigDecimal().also {
                    val serialized = objectWriter.writeValueAsString(it)
                    assertEquals("100", serialized)
                }

                "100.12000".toBigDecimal().also {
                    val serialized = objectWriter.writeValueAsString(it)
                    assertEquals("100.12", serialized)
                }

            }

        }.testBigDecimalSerialization()
    }

    @Test
    fun testVerifyAndFail() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(
                    """
                    {"status":"error",
                     "message":"test error"}
                    """
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }


        val client = object : APIClient(HttpClient(mockEngine)) {
            suspend fun latestBuyPrice(assetType: AssetType): BigDecimal =
                httpClient.get("stub").run {
                    processResponse(this, RateResponse::class.java) { it.rate }
                }
        }

        runBlocking {
            assertThrows<CoinspotApiException> { client.latestBuyPrice(AssetType.AGIX) }
        }
    }

    @Test
    fun testVerifyAndSucceed() {
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel(
                    """
                    {"status":"ok",
                     "rate":123,
                     "market":"test"}
                    """
                ),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }


        val client = object : APIClient(HttpClient(mockEngine)) {
            suspend fun latestBuyPrice(assetType: AssetType): BigDecimal =
                httpClient.get("stub").run {
                    processResponse(this, RateResponse::class.java) { it.rate }
                }
        }

        runBlocking {
            assertEquals(0, client.latestBuyPrice(AssetType.AGIX).compareTo(BigDecimal(123)))
        }
    }
}