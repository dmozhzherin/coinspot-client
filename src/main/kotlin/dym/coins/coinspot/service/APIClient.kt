package dym.coins.coinspot.service

import com.fasterxml.jackson.databind.ObjectReader
import com.fasterxml.jackson.databind.ObjectWriter
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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

    private fun <T> verify(response: HttpResponse<T>) =
        if (response.statusCode() == HTTP_OK) true
        else {
            val content = if (response.body() is InputStream) {
                val body = response.body() as InputStream
                body.bufferedReader().readText()
            } else ""
            throw CoinspotException("API call failed. Status ${response.statusCode()} body: $content")
        }

    protected fun <T, P : ResponseMeta> processResponse(
        response: HttpResponse<InputStream>,
        clazz: Class<P>,
        transform: (P) -> T
    ): T {

        verify(response)
        objectReader.readValue(response.body(), clazz).run {
            if (isSuccess()) return transform(this);
            else throw CoinspotException("API call failed: $message")
        }
    }

    protected companion object {
        val objectWriter: ObjectWriter
        val objectReader: ObjectReader

        init {
            val jsonMapper = JsonMapper()
            jsonMapper.registerModule(JavaTimeModule())
            objectWriter = jsonMapper.writer()
            objectReader = jsonMapper.reader()
        }
    }
}