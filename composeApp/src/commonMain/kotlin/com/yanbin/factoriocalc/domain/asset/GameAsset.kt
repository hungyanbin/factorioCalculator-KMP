package com.yanbin.factoriocalc.domain.asset

/**
 * A drawable region of a sprite sheet: which sheet ([url]) and where the cell
 * lives within it ([offset] + [size]). Mirrors the reference site's
 * `background-position` / `background-size` model.
 */
data class GameAsset(
    val url: String,
    val offset: Position,
    val size: Size,
)
