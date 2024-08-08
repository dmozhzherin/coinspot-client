package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

/**
 * @author dym
 * Date: 06.08.2024
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class RateResponse(
    val rate: BigDecimal,
    val market:String
)
