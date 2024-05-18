package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter
import java.util.TreeMap

/**
 * @author dym
 * Date: 29.10.2023
 */
@JvmRecord
@JsonIgnoreProperties(ignoreUnknown = true)
data class BalancesResponse (
    override val status: String,
    override val message: String?,
    @JsonDeserialize(converter = CoinspotBalancesArrayToMapConverter::class)
    @JvmField val balances: Map<String, Balance>
) : ResponseMeta {

}

/**
 * Well, it's not me - it's the authors of Coinspot API.
 */
class CoinspotBalancesArrayToMapConverter : StdConverter<Array<Map<String, Balance>>, Map<String, Balance>>() {
    override fun convert(value: Array<Map<String, Balance>>): Map<String, Balance> {
        return TreeMap<String, Balance>().apply {
            value.forEach { putAll(it) }
        }
    }
}
