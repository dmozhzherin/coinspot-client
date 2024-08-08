package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal

/**
 * @author dym
 * Date: 18.05.2024
 */
@JvmRecord
@JsonIgnoreProperties(ignoreUnknown = true)
data class SwapNowResponse(
    val coin: AssetType,
    val market: String,
    val amount: BigDecimal,
    val rate: BigDecimal,
    val total: BigDecimal
)
