package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.ZonedDateTime

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

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JvmRecord
    data class TradeOperation(
        val coin: String,
        val type: OperationType,
        val market: String,
        val rate: BigDecimal,
        val amount: BigDecimal,
        val total: BigDecimal,
        val solddate: ZonedDateTime,
        val audfeeExGst: BigDecimal,
        val audGst: BigDecimal,
        val audtotal: BigDecimal
    )
}