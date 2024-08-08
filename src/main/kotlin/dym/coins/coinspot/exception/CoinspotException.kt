package dym.coins.coinspot.exception

/**
 * @author dym
 * Date: 15.09.2023
 */
open class CoinspotException (
    val status: String?,
    message: String?,
    cause: Throwable? = null
) : Exception(message, cause) {
    constructor(message: String) : this(null, message)
    constructor(message: String, cause: Throwable) : this(null, message, cause)
}