package dym.coins.coinspot.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal
import java.time.Instant

@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class TransferOperation (
    val timestamp: Instant,
    val amount: BigDecimal,
    val coin: AssetType,
    val address: String,
    val aud: BigDecimal = BigDecimal.ZERO,
)