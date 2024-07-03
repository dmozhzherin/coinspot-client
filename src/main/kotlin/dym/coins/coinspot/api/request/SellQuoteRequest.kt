package dym.coins.coinspot.api.request

import com.fasterxml.jackson.annotation.JsonValue
import dym.coins.coinspot.domain.AssetType
import java.math.BigDecimal

/**
 * @author dym
 * Date: 17.05.2024
 */
data class SellQuoteRequest(
    val cointype: AssetType,
    val amount: BigDecimal,
    val amounttype: AmountType = AmountType.COIN,
): HMACRequest() {

   enum class AmountType(@JsonValue val value: String) {
        AUD("aud"),
        COIN("coin")
    }
}
