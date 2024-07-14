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
    /**
     * Amount of the asset that was swapped
     */
    val amount: BigDecimal,
    /**
     * Rate at which the swap was made
     */
    val rate: BigDecimal,
    /**
     * Total amount of the asset that was received
     */
    val total: BigDecimal
)
