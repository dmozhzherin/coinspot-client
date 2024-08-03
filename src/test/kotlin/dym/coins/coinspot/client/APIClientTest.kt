package dym.coins.coinspot.client

import org.junit.jupiter.api.Assertions.assertEquals
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
}