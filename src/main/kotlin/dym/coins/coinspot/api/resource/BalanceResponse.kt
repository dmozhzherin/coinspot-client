package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter
import dym.coins.coinspot.domain.AssetType

/**
 * @author dym
 * Date: 30.10.2023
 */
@JvmRecord
@JsonIgnoreProperties(ignoreUnknown = true)
data class BalanceResponse(
    @JsonDeserialize(converter = CoinspotBalanceSingletonMapUnwrapper::class)
    val balance: Balance
)

class CoinspotBalanceSingletonMapUnwrapper : StdConverter<Map<AssetType, Balance>, Balance>() {
    override fun convert(value: Map<AssetType, Balance>): Balance {
        return value.values.first()
    }
}