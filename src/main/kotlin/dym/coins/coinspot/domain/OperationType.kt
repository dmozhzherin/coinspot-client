package dym.coins.coinspot.domain

import com.fasterxml.jackson.annotation.JsonValue

/**
 * @author dym
 * Date: 15.09.2023
 */
enum class OperationType {
    MARKET, INSTANT, TAKE_PROFIT, STOP_LOSS, LIMIT, BUY_STOP, DEPOSIT, WITHDRAWAL, TRANSFER, UNKNOWN;

    @JsonValue
    fun toValue(): String = name.lowercase().replace("_", " ")
}