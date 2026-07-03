package com.yanbin.factoriocalc.domain.asset

/**
 * A game entity paired with the sprite-sheet region that draws it.
 * Implemented by the per-category domain classes in `domain.dataset`
 * (e.g. `Belt`, `Boiler`, `Recipe`), each adding its own gameplay fields.
 */
interface Sprite {
    val id: String
    val name: String
    val asset: GameAsset
}
