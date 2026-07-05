package com.yanbin.factoriocalc.domain.asset

/**
 * A game entity paired with a loadable sprite URI (see `com.yanbin.factoriocalc.data.SpriteUri`).
 * Implemented by the per-category domain classes in `domain.dataset`
 * (e.g. `Belt`, `Boiler`, `Recipe`), each adding its own gameplay fields.
 */
interface Sprite {
    val name: String
    val uri: String
}
