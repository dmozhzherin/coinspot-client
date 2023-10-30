package dym.coins.coinspot.api.request

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * @author dym
 * Date: 15.09.2023
 */
@JvmRecord
@JsonInclude(JsonInclude.Include.NON_NULL)
data class OrderHistoryRequest (
    val cointype : String?,
    val markettype : String?,

    override val startdate: String?,
    override val enddate: String?,

    val limit : Int?,

    override val nonce : Nonce = Nonce(),

    ) : DateIntervalRequest, HMACRequest
