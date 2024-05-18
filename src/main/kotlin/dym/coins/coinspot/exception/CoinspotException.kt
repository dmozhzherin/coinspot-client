package dym.coins.coinspot.exception

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotException (
    val error: String?,
    message: String?,
    cause: Throwable? = null
) : Throwable(message, cause) {
    constructor(message: String) : this(null, message)
    constructor(message: String, cause: Throwable) : this(null, message, cause)
}