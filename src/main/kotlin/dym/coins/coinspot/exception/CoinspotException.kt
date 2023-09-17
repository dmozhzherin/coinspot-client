package dym.coins.coinspot.exception

/**
 * @author dym
 * Date: 15.09.2023
 */
class CoinspotException : Throwable {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}