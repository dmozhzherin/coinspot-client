package dym.coins.coinspot.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertSame

/**
 * @author dym
 * Date: 25.08.2024
 */
class AssetTypeTest {

    @Test
    fun testOf() {
        assertEquals(AssetType.XLM, AssetType.of("xlm"));
        assertEquals(AssetType.of("XXX"), AssetType.of("xxx"));
        val expected = AssetType.of("XXX")
        val actual = AssetType.of("xxx")
        println("expected: ${expected.code}")
        println("actual: ${actual.code}")
        assertSame(expected, actual);
    }
}