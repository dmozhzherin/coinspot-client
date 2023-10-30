package dym.coins.coinspot.api.request

/**
 * @author dym
 * Date: 17.09.2023
 */
interface HMACRequest {
    val nonce: Nonce

    companion object {
        fun noinput() = object : HMACRequest {
            override val nonce = Nonce()
        }
    }
}