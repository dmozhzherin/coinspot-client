package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.ZonedDateTime

/**
 * @author dym
 * Date: 15.09.2023
 */
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
