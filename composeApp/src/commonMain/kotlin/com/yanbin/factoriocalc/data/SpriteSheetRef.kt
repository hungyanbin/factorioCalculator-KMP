package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.asset.GameAsset
import com.yanbin.factoriocalc.domain.asset.Position
import com.yanbin.factoriocalc.domain.asset.Size
import kotlinx.serialization.Serializable

/** The sprite sheet every category's `iconCol`/`iconRow` indexes into. */
@Serializable
internal data class SpriteSheetRef(
    val url: String,
    val cell: Int,
)

internal fun SpriteSheetRef.assetFor(iconCol: Int, iconRow: Int): GameAsset =
    GameAsset(
        url = url,
        offset = Position(iconCol * cell, iconRow * cell),
        size = Size(cell, cell),
    )
