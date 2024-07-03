package dym.coins.coinspot.api.request

import com.fasterxml.jackson.annotation.JsonInclude
import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal

/**
 * @author dym
 * Date: 18.05.2024
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class SwapNowRequest(
    val cointypesell: AssetType,
    val cointypebuy: AssetType,
    val amount: BigDecimal,

    val rate: BigDecimal?,
    val treshold: BigDecimal?
): HMACRequest()
