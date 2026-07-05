package com.yanbin.factoriocalc.data.serialization

import coil3.Uri
import coil3.pathSegments

/**
 * The `sprite://` scheme addresses a single cell of a bundled sprite sheet, e.g.
 * `sprite://sprite-sheet-<hash>.png/14x1?cell=32` — self-describing so [com.yanbin.factoriocalc.coil.SpriteFetcher]
 * needs no shared lookup state to resolve it back to pixels.
 */
private const val SPRITE_SCHEME = "sprite"

internal data class ParsedSpriteUri(
    val sheetBasename: String,
    val col: Int,
    val row: Int,
    val cell: Int,
)

internal fun spriteUri(sheetBasename: String, col: Int, row: Int, cell: Int): String =
    "$SPRITE_SCHEME://$sheetBasename/${col}x$row?cell=$cell"

internal fun parseSpriteUri(uri: Uri): ParsedSpriteUri? {
    if (uri.scheme != SPRITE_SCHEME) return null
    val sheetBasename = uri.authority ?: return null
    val coordinates = uri.pathSegments.firstOrNull() ?: return null
    val (colPart, rowPart) = coordinates.split("x", limit = 2).takeIf { it.size == 2 } ?: return null
    val col = colPart.toIntOrNull() ?: return null
    val row = rowPart.toIntOrNull() ?: return null
    val cell = uri.query?.substringAfter("cell=", "")?.substringBefore('&')?.toIntOrNull() ?: return null
    return ParsedSpriteUri(sheetBasename, col, row, cell)
}
