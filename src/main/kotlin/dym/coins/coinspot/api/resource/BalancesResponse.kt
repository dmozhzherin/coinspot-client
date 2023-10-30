package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter
import java.math.BigDecimal
import java.util.TreeMap

/**
 * @author dym
 * Date: 29.10.2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class BalancesResponse (
    override val status: String,
    override val message: String?,
    @JsonDeserialize(converter = CoinspotBakancesArrayToMapConverter::class)
    @JvmField val balances: Map<String, Balance>
) : ResponseMeta {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JvmRecord
    data class Balance(
        val balance: BigDecimal,
        val audbalance: BigDecimal,
        val rate: BigDecimal,
    )
}

/**
 * Well, it's not me - it's the authors of Coinspot API.
 */
class CoinspotBakancesArrayToMapConverter
    : StdConverter<Array<Map<String, BalancesResponse.Balance>>, Map<String, BalancesResponse.Balance>>() {
    override fun convert(value: Array<Map<String, BalancesResponse.Balance>>): Map<String, BalancesResponse.Balance> {
        return TreeMap<String, BalancesResponse.Balance>().apply {
            value.forEach { putAll(it) }
        }
    }
}
