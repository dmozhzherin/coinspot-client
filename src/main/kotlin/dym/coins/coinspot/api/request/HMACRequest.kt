package dym.coins.coinspot.api.request

import java.security.SecureRandom
import java.util.concurrent.atomic.LongAdder

/**
 * @author dym
 * Date: 17.09.2023
 */
abstract class HMACRequest {
    var nonce: Long = -1
        private set

    /**
     * Generate and set nonce
     */
    fun nonced(): HMACRequest = apply { nonce = nonce() }

    private fun nonce() = counter.run {
        add(random.nextLong(RANDOM_SHIFT) + 1)
        sum()
    }

    companion object {
        private const val RANDOM_SHIFT = 10L

        @JvmStatic
        private val counter = LongAdder().apply { add(System.currentTimeMillis()) }

        @JvmStatic
        private val random = SecureRandom()

        @JvmStatic
        fun noinput() = object : HMACRequest() {}
    }

}