package dym.coins.coinspot.api.request

/**
 * @author dym
 * Date: 15.09.2023
 */
@JvmRecord
data class OrderHistoryRequest (
    val cointype : String?,
    val markettype : String?,
    val startdate : String?,
    val enddate : String?,
    val limit : Int?,

    override val nonce : Long = System.currentTimeMillis()
) : HMACRequest
