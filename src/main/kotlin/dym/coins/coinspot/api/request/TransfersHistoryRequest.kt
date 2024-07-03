package dym.coins.coinspot.api.request

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * @author dym
 * Date: 18.09.2023
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class TransfersHistoryRequest (
    override val startdate: String?,
    override val enddate: String?,
    override var nonce: Long = -1
): DateIntervalRequest, HMACRequest
