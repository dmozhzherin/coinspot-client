package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import dym.coins.coinspot.api.dto.TransferOperation

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
}


