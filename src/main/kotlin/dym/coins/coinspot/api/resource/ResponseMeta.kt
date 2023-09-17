package dym.coins.coinspot.api.resource

/**
 * @author dym
 * Date: 15.09.2023
 */
interface ResponseMeta {
    val status: String
    val message: String?
    fun isSuccess() = "ok".equals(status, ignoreCase = true)
}