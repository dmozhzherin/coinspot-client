package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import dym.coins.coinspot.domain.AssetType
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
    @JsonDeserialize(keyAs = AssetType::class)
    @JvmField val prices: Map<AssetType, Rate>
) : ResponseMeta {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JvmRecord
    data class Rate(val bid: BigDecimal, val ask: BigDecimal, val last: BigDecimal)
}
