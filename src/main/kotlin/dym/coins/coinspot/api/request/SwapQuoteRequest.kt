package dym.coins.coinspot.api.request

import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal

/**
 * @author dym
 * Date: 17.05.2024
 */
@JvmRecord
data class SwapQuoteRequest(
    val cointypesell: AssetType,
    val cointypebuy: AssetType,
    val amount: BigDecimal,

    override val nonce : Nonce = Nonce(),
): HMACRequest
