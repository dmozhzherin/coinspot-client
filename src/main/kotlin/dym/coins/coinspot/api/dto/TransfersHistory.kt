package dym.coins.coinspot.api.dto

/**
 * @author dym
 * Date: 18.05.2024
 */
@JvmRecord
data class TransfersHistory(
    @JvmField val sendtransactions : List<TransferOperation>,
    @JvmField val receivetransactions : List<TransferOperation>
)
