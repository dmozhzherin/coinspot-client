package dym.coins.coinspot.client

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dym.coins.coinspot.api.resource.ResponseMeta
import dym.coins.coinspot.exception.CoinspotException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode

/**
 * @author dym
 * Date: 15.09.2023
 */
abstract class APIClient {

    private suspend fun verify(response: HttpResponse) =
        if (response.status == HttpStatusCode.OK) true
        else throw CoinspotException("API call failed. Status ${response.status}, Body: ${response.body<String>()}")

    protected suspend fun <T, P : ResponseMeta> processResponse(
        response: HttpResponse,
        clazz: Class<P>,
        transform: (P) -> T
    ): T {
        verify(response)
        try {
            objectReader.readValue(response.body<ByteArray>(), clazz)
        } catch (e: JacksonException) {
            throw CoinspotException("API call failed. Status ${response.status}", e)
        }.run {
            if (isSuccess()) return transform(this)
            else throw CoinspotException(status, "API call failed: $message")
        }
    }

    protected companion object {
        val objectWriter: ObjectWriter
        val objectReader: ObjectReader

        init {
            val jsonMapper = JsonMapper.builder()
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .addModule(JavaTimeModule())
                .build()
            jsonMapper.registerKotlinModule()
            objectWriter = jsonMapper.writer()
            objectReader = jsonMapper.reader()
        }
    }
}