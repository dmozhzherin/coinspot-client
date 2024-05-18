package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dym.coins.coinspot.api.dto.TradeOperation

/**
 * @author dym
 * Date: 12.02.2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class OrderHistoryResponse(
    override val status: String,
    override val message: String?,
    @JvmField val buyorders: List<TradeOperation>,
    @JvmField val sellorders: List<TradeOperation>
) : ResponseMeta {

}