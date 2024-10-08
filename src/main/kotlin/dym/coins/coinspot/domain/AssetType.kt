package dym.coins.coinspot.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * @author dym
 * Date: 27.09.2023
 */
/**
 * Asset type to identify an asset in the account.
 * A simple String could have been used, but Coinspot API uses different codes for the same asset.
 * Hopefully, there are only two different codes for the same asset. Known aliases are hardcoded here.
 * A database table with all asset types and all aliases would be nice to have in the future if the project grows
 */
@JvmRecord
data class AssetType internal constructor(
    /**
     * Asset code used in the API (e.g. "SCRT" for Secret, "XLM" for Stellar, "AUD" for Australian Dollar, etc.
     */
    @JsonValue val code: String,
    /**
     * Asset name (e.g "Secret", "Stellar", "Australian Dollar", etc.)
     */
    val name: String,
    /**
     * Alias used in the API for the same asset (e.g. "ENG" for Secret, "STR" for Stellar, etc.)
     */
    val alias: String?,
    val isFiat: Boolean = false
) : Comparable<AssetType> {

    override fun compareTo(other: AssetType) = code.compareTo(other.code)

    companion object {

        val AUD = AssetType("AUD", "Australian Dollar", null, true)
        val SCRT = AssetType("SCRT", "Secret", "ENG", false)
        val XLM = AssetType("XLM", "Stellar", "STR", false)
        val COCOS = AssetType("COCOS", "Cocos-BCX", "COMBO", false)
        val AGIX = AssetType("AGIX", "SingularityNET", "AGI", false)
        val LUNA = AssetType("LUNA", "Terra", "LUNC", false)
        val BADGER = AssetType("BADGER", "Badger", "BDGR", false)
        val BTT = AssetType("BTT", "BitTorrent", "BTTC", false)
        val REV = AssetType("REV", "Revain", "R", false)
        val BCC = AssetType("BCC", "Bitcoin Cash", "BCH", false)

        @JvmStatic
        private val assetTypes: ConcurrentMap<String, AssetType> = ConcurrentHashMap(mapOf(
            AUD.code to AUD,

            SCRT.code to SCRT,
            SCRT.alias!! to SCRT,

            XLM.code to XLM,
            XLM.alias!! to XLM,

            COCOS.code to COCOS,
            COCOS.alias!! to COCOS,

            AGIX.code to AGIX,
            AGIX.alias!! to AGIX,

            LUNA.code to LUNA,
            LUNA.alias!! to LUNA,

            BADGER.code to BADGER,
            BADGER.alias!! to BADGER,

            BTT.code to BTT,
            BTT.alias!! to BTT,

            REV.code to REV,
            REV.alias!! to REV,

            BCC.code to BCC,
            BCC.alias!! to BCC
        ))

        @JvmStatic
        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        fun of(code: String) = assetTypes.computeIfAbsent(code.uppercase()) { key -> AssetType(key, key, null) }
    }

}