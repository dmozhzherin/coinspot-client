package dym.coins.coinspot.api.dto

/**
 * @author dym
 * Date: 18.05.2024
 */
@JvmRecord
data class OrderHistory (
    @JvmField val buyorders: List<TradeOperation>,
    @JvmField val sellorders: List<TradeOperation>
)