package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

/**
 * @author dym
 * Date: 17.05.2024
 */
@JvmRecord
@JsonIgnoreProperties(ignoreUnknown = true)
data class SwapQuoteResponse(
    val rate: BigDecimal
)
