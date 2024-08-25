package dym.coins.coinspot.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * @author dym
 * Date: 25.08.2024
 */
class AssetTypeTest {

    @Test
    fun testOf() {
        assertEquals(AssetType.XLM, AssetType.of("xlm"));
        assertEquals(AssetType.of("XXX"), AssetType.of("xxx"));
    }
}