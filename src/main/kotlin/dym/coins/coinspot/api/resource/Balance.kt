package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal

@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class Balance(
    val balance: BigDecimal,
    val audbalance: BigDecimal,
    val rate: BigDecimal,
    val available: BigDecimal?
)