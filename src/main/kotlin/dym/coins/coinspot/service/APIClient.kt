package dym.coins.coinspot.service

import com.fasterxml.jackson.core.JacksonException
import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dym.coins.coinspot.api.resource.ResponseMeta
import dym.coins.coinspot.exception.CoinspotException
import java.io.InputStream
import java.net.HttpURLConnection.HTTP_OK
import java.net.http.HttpResponse

/**
 * @author dym
 * Date: 15.09.2023
 */
abstract class APIClient {

    private fun <T> readBody(response: HttpResponse<T>): String {
        return if (response.body() is InputStream) {
            val body = response.body() as InputStream
            body.bufferedReader().readText()
        } else ""
    }

    private fun <T> verify(response: HttpResponse<T>) =
        if (response.statusCode() == HTTP_OK) true
        else throw CoinspotException("API call failed. Status ${response.statusCode()} body: ${readBody(response)}")

    protected fun <T, P : ResponseMeta> processResponse(
        response: HttpResponse<InputStream>,
        clazz: Class<P>,
        transform: (P) -> T
    ): T {

        verify(response)
        try {
            objectReader.readValue(response.body(), clazz)
        } catch (e: JacksonException) {
            throw CoinspotException("API call failed. Status ${response.statusCode()}", e)
        }.run {
            if (isSuccess()) return transform(this)
            else throw CoinspotException("API call failed: $message")
        }
    }

    protected companion object {
        val objectWriter: ObjectWriter
        val objectReader: ObjectReader

        init {
            val jsonMapper = JsonMapper()
            jsonMapper.registerKotlinModule()
            jsonMapper.registerModule(JavaTimeModule())
            objectWriter = jsonMapper.writer()
            objectReader = jsonMapper.reader()
        }
    }
}