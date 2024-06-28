package dym.coins.coinspot.api.request

/**
 * @author dym
 * Date: 30.10.2023
 */
@JvmInline
value class Nonce(val value : Long = System.nanoTime())