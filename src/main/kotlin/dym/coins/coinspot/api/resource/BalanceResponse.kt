package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * @author dym
 * Date: 30.10.2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class BalanceResponse(
    override val status: String,
    override val message: String?,
    @JsonDeserialize(converter = CoinspotBalanceSingletonMapUnwrapper::class)
    val balance: Balance
) : ResponseMeta

class CoinspotBalanceSingletonMapUnwrapper : StdConverter<Map<String, Balance>, Balance>() {
    override fun convert(value: Map<String, Balance>): Balance {
        return value.values.first()
    }
}