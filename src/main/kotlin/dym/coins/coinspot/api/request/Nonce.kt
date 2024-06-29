package dym.coins.coinspot.api.request

import java.security.SecureRandom
import java.util.concurrent.atomic.LongAdder

/**
 * @author dym
 * Date: 30.10.2023
 */
@JvmInline
value class Nonce(val value : Long = counter.run { add(random.nextLong(RANDOM_SHIFT) + 1); sum()}) {

    companion object {
        private const val RANDOM_SHIFT = 10L

        @JvmStatic
        val counter = LongAdder().also { it.add(System.currentTimeMillis())}
        @JvmStatic
        val random = SecureRandom()
    }
}

