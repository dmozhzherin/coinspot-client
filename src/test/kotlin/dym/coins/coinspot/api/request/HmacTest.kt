package dym.coins.coinspot.api.request

import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test

/**
 * @author dym
 * Date: 29.06.2024
 */
class HmacTest {

    @Test
    fun testNoInputHasIncrementingNonce() {
        val request1 = HMACRequest.noinput()
        val request2 = HMACRequest.noinput()

        println(request1.nonce)
        println(request2.nonce)

        assertTrue(request1.nonce.value < request2.nonce.value)
    }
}