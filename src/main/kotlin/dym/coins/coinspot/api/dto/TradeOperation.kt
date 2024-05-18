package dym.coins.coinspot.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dym.coins.coinspot.domain.AssetType
import dym.coins.coinspot.domain.OperationType
import java.math.BigDecimal
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class TradeOperation(
    val coin: AssetType,
    val type: OperationType,
    val market: String,
    val rate: BigDecimal,
    val amount: BigDecimal,
    val total: BigDecimal,
    val solddate: Instant,
    val audfeeExGst: BigDecimal,
    val audGst: BigDecimal,
    val audtotal: BigDecimal
)