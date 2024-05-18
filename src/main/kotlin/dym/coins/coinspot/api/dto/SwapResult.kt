package dym.coins.coinspot.api.dto

import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal

/**
 * @author dym
 * Date: 18.05.2024
 */
@JvmRecord
data class SwapResult(
    val from: AssetType,
    val to: AssetType,
    val amount: BigDecimal,
    val rate: BigDecimal,
    val total: BigDecimal
)
