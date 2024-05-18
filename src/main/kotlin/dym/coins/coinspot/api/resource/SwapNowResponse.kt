package dym.coins.coinspot.api.resource

import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal

/**
 * @author dym
 * Date: 18.05.2024
 */
@JvmRecord
data class SwapNowResponse(
    override val status: String,
    override val message: String?,

    val coin: AssetType,
    val market: String,
    val amount: BigDecimal,
    val rate: BigDecimal,
    val total: BigDecimal

) : ResponseMeta
