package dym.coins.coinspot.exception

/**
 * @author dym
 * Date: 06.08.2024
 */
class CoinspotApiException(
    status: String,
    message: String?,
    cause: Throwable? = null
) : CoinspotException(status, message, cause)