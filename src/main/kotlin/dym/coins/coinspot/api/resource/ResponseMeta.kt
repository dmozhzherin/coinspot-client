package dym.coins.coinspot.api.resource

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * @author dym
 * Date: 15.09.2023
 */
interface ResponseMeta {
    val status: String
    val message: String?
    @JsonIgnore
    fun isSuccess() = "ok".equals(status, ignoreCase = true)
}