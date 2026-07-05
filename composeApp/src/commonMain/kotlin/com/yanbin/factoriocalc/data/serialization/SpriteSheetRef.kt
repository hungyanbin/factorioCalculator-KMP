package com.yanbin.factoriocalc.data.serialization

import kotlinx.serialization.Serializable

/** The sprite sheet every category's `iconCol`/`iconRow` indexes into. */
@Serializable
internal data class SpriteSheetRef(
    val url: String,
    val cell: Int,
)

internal fun SpriteSheetRef.uriFor(iconCol: Int, iconRow: Int): String =
    spriteUri(
        sheetBasename = url.substringAfterLast('/'),
        col = iconCol,
        row = iconRow,
        cell = cell
    )
