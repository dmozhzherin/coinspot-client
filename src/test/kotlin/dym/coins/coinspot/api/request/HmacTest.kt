package dym.coins.coinspot.api.request

import org.junit.jupiter.api.Assertions.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * @author dym
 * Date: 29.06.2024
 */
class HmacTest {

    @Test
    fun testNoInputHasIncrementingNonce() {
        val request1 = HMACRequest.noinput()
        val request2 = HMACRequest.noinput()

        assertEquals(request2.nonce, request1.nonce)

        assertTrue(request1.nonced().nonce < request2.nonced().nonce)
    }
}