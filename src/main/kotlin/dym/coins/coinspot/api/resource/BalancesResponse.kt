package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter
import dym.coins.coinspot.domain.AssetType
import java.util.TreeMap

/**
 * @author dym
 * Date: 29.10.2023
 */
@JvmRecord
@JsonIgnoreProperties(ignoreUnknown = true)
data class BalancesResponse (
    @JsonDeserialize(converter = CoinspotBalancesArrayToMapConverter::class)
    val balances: Map<AssetType, Balance>
)

/**
 * Well, it's not me - it's the authors of Coinspot API.
 */
class CoinspotBalancesArrayToMapConverter : StdConverter<Array<Map<AssetType, Balance>>, Map<AssetType, Balance>>() {
    override fun convert(value: Array<Map<AssetType, Balance>>): Map<AssetType, Balance> {
        return TreeMap<AssetType, Balance>().apply {
            value.forEach { putAll(it) }
        }
    }
}
