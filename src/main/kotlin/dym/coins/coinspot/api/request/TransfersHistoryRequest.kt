package dym.coins.coinspot.api.request

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * @author dym
 * Date: 18.09.2023
 */
@JvmRecord
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TransfersHistoryRequest (
    override val startdate: String?,
    override val enddate: String?,
    override val nonce: Nonce = Nonce()
): DateIntervalRequest, HMACRequest
