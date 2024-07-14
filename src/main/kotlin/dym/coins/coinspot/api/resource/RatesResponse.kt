package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
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
    @JsonDeserialize(using = RateDeserializer::class)
    data class Rate(val bid: BigDecimal, val ask: BigDecimal, val last: BigDecimal) {

        companion object {
            val FAULT = BigDecimal.ONE.negate().let { Rate(it, it, it) }
        }
    }

    /**
     * Dynamic typing must be concidered a criminal offense punishable by death.
     * Coinspot API out of the blue may return a string "NaN" instead of a number.
     * This is a workaround to prevent the application from crashing and dropping
     * the whole list of rates.
     */
    class RateDeserializer : StdDeserializer<Rate>(Rate::class.java) {
        override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): Rate =
            p0?.codec?.readTree<JsonNode>(p0)?.runCatching {
                Rate(
                    bid = get("bid").asText().toBigDecimal(),
                    ask = get("ask").asText().toBigDecimal(),
                    last = get("last").asText().toBigDecimal())
            }?.getOrDefault(Rate.FAULT)?: Rate.FAULT
    }
}
