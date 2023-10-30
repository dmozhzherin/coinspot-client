package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

/**
 * @author dym
 * Date: 12.02.2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class RatesResponse(
    override val status: String,
    override val message: String?,
    @JvmField val prices: Map<String, Rate>
) : ResponseMeta {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JvmRecord
    data class Rate(val bid: BigDecimal, val ask: BigDecimal, val last: BigDecimal)
}
