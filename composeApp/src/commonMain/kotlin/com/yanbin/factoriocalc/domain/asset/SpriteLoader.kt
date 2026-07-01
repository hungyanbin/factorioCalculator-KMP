package com.yanbin.factoriocalc.domain.asset

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/** Base sprite-sheet cell size, mirroring the reference site's PX_WIDTH/PX_HEIGHT. */
const val SPRITE_CELL_SIZE = 32

/**
 * Parses a Factorio dataset JSON (the same shape the reference site serves) into
 * [Sprite]s. Every top-level array of entities is scanned; any element carrying
 * `key` + `icon_col` + `icon_row` becomes a [Sprite]. Entries without those
 * fields (e.g. `groups` metadata) are skipped.
 */
class SpriteLoader(
    private val json: Json = Json { ignoreUnknownKeys = true },
    private val cell: Int = SPRITE_CELL_SIZE,
) {
    fun parse(datasetJson: String): List<Sprite> {
        val root = json.parseToJsonElement(datasetJson).jsonObject
        val url = spriteSheetUrl(root) ?: return emptyList()

        return root.values
            .filterIsInstance<JsonArray>()
            .flatMap { array -> array.filterIsInstance<JsonObject>() }
            .mapNotNull { entity -> entity.toSpriteOrNull(url) }
            .distinctBy { sprite -> sprite.id }
    }

    private fun spriteSheetUrl(root: JsonObject): String? {
        val hash = (root["sprites"] as? JsonObject)
            ?.get("hash")
            ?.jsonPrimitive
            ?.contentOrNull
            ?: return null
        return "images/sprite-sheet-$hash.png"
    }

    private fun JsonObject.toSpriteOrNull(url: String): Sprite? {
        val key = (this["key"] as? JsonPrimitive)?.contentOrNull ?: return null
        val col = (this["icon_col"] as? JsonPrimitive)?.intOrNull ?: return null
        val row = (this["icon_row"] as? JsonPrimitive)?.intOrNull ?: return null

        val asset = GameAsset(
            url = url,
            offset = Position(col * cell, row * cell),
            size = Size(cell, cell),
        )
        val name = englishName() ?: return null
        return Sprite(
            asset = asset,
            id = key,
            name = name,
            properties = scalarProperties(),
        )
    }

    private fun JsonObject.englishName(): String? =
        (this["localized_name"] as? JsonObject)
            ?.get("en")
            ?.jsonPrimitive
            ?.contentOrNull

    /** Remaining scalar fields, excluding those already promoted to typed fields. */
    private fun JsonObject.scalarProperties(): Map<String, String> =
        entries
            .filter { (name, _) -> name !in PROMOTED_FIELDS }
            .mapNotNull { (name, value) ->
                (value as? JsonPrimitive)?.contentOrNull?.let { name to it }
            }
            .toMap()

    private companion object {
        val PROMOTED_FIELDS = setOf("key", "icon_col", "icon_row", "localized_name")
    }
}
