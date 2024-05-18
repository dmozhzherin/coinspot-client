package dym.coins.coinspot.api.resource

import java.math.BigDecimal

/**
 * @author dym
 * Date: 17.05.2024
 */
@JvmRecord
data class SwapQuoteResponse(
    override val status: String,
    override val message: String?,
    val rate: BigDecimal
) : ResponseMeta
