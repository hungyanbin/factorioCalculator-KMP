package com.yanbin.factoriocalc.domain.asset

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SpriteLoaderTest {

    private val fixture = """
        {
            "sprites": { "hash": "abc123" },
            "belts": [
                {
                    "key": "express-transport-belt",
                    "icon_col": 12,
                    "icon_row": 5,
                    "localized_name": { "en": "Express transport belt" },
                    "speed": 0.09375
                }
            ],
            "items": [
                {
                    "key": "iron-plate",
                    "icon_col": 0,
                    "icon_row": 1,
                    "localized_name": { "en": "Iron plate" }
                }
            ],
            "groups": [
                { "key": "logistics", "order": "a" }
            ]
        }
    """.trimIndent()

    @Test
    fun parsesEntityWithIconIntoSprite() {
        val sprites = SpriteLoader().parse(fixture)

        val belt = sprites.single { it.id == "express-transport-belt" }
        assertEquals(
            GameAsset(
                url = "images/sprite-sheet-abc123.png",
                offset = Position(12 * 32, 5 * 32),
                size = Size(32, 32),
            ),
            belt.asset,
        )
        assertEquals("Express transport belt", belt.name)
        assertEquals("0.09375", belt.properties["speed"])
    }

    @Test
    fun skipsEntriesWithoutIconFields() {
        val sprites = SpriteLoader().parse(fixture)

        // groups has no icon_col/icon_row -> not a sprite.
        assertTrue(sprites.none { it.id == "logistics" })
        // Only the belt and the item become sprites.
        assertEquals(2, sprites.size)
    }

    @Test
    fun promotedFieldsAreNotDuplicatedInProperties() {
        val belt = SpriteLoader().parse(fixture).single { it.id == "express-transport-belt" }

        assertNull(belt.properties["key"])
        assertNull(belt.properties["icon_col"])
        assertNull(belt.properties["icon_row"])
        assertNull(belt.properties["localized_name"])
    }

    @Test
    fun returnsEmptyWhenNoSpriteSheetHash() {
        val sprites = SpriteLoader().parse("""{ "items": [] }""")
        assertTrue(sprites.isEmpty())
    }
}
