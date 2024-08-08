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
    @JvmField val sendtransactions : List<TransferOperation>,
    @JvmField val receivetransactions : List<TransferOperation>
)

