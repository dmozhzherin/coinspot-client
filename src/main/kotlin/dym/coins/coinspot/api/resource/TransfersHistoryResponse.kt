package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.math.BigDecimal
import java.time.ZonedDateTime

/**
 * @author dym
 * Date: 18.09.2023
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JvmRecord
data class TransfersHistoryResponse(
    override val status: String,
    override val message: String?,

    @JvmField val sendtransactions : List<TransferOperation>,
    @JvmField val receivetransactions : List<TransferOperation>

) : ResponseMeta {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JvmRecord
    data class TransferOperation (
        val timestamp: ZonedDateTime,
        val amount: BigDecimal,
        val coin: String,
        val address: String,
        val aud: BigDecimal? = BigDecimal.ZERO,
    )
}


