package dym.coins.coinspot.client

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dym.coins.coinspot.exception.CoinspotApiException
import dym.coins.coinspot.exception.CoinspotException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author dym
 * Date: 15.09.2023
 */
abstract class APIClient(protected val httpClient: HttpClient = HttpClient(CIO)) : AutoCloseable {

    override fun close() {
        httpClient.close()
    }

    private suspend fun verify(response: HttpResponse) =
        if (response.status == HttpStatusCode.OK) true
        else throw CoinspotException("API call failed: ${response.request.url}, Status ${response.status}, Body: ${response.body<String>()}")

    protected suspend fun <T, P> processResponse(
        response: HttpResponse,
        clazz: Class<P>,
        transform: (P) -> T
    ): T {
        verify(response)
        try {
            objectReader.readTree(response.body<ByteArray>())
        } catch (e: JacksonException) {
            throw CoinspotException("API call failed. Status ${response.status}", e)
        }.run {
            get(FIELD_STATUS).asText().let { status ->
                if (OK.equals(status, ignoreCase = true)) {
                    try {
                        return transform(objectReader.readValue(this, clazz))
                    } catch (e: JacksonException) {
                        throw CoinspotException("API call failed. Status ${response.status}", e)
                    }
                } else {
                    throw CoinspotApiException(status, get("message").asText())
                }
            }
        }
    }

    protected companion object {
        private const val FIELD_STATUS = "status"
        private const val OK = "ok"
        private const val API_PRECISION = 8

        val objectWriter: ObjectWriter
        val objectReader: ObjectReader

        init {
            val jsonMapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .addModule(JavaTimeModule())
                .build()

            jsonMapper.registerKotlinModule()

            SimpleModule().apply {
                addSerializer(BigDecimal::class.java,BigDecimalSerializer )
            }.let { jsonMapper.registerModule(it) }

            objectWriter = jsonMapper.writer()
            objectReader = jsonMapper.reader()
        }
    }

    protected object BigDecimalSerializer : JsonSerializer<BigDecimal?>() {
        override fun serialize(value: BigDecimal?, gen: JsonGenerator?, serializers: SerializerProvider?) {
            value?.setScale(API_PRECISION, RoundingMode.HALF_EVEN)
                ?.stripTrailingZeros()
                ?.toPlainString()
                ?.let { gen?.writeNumber(it) }
        }
    }
}