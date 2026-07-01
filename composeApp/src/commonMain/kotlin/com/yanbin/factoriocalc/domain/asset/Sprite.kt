package com.yanbin.factoriocalc.domain.asset

/**
 * A game entity paired with the sprite-sheet region that draws it.
 *
 * @param asset where to find this sprite's pixels.
 * @param id the entity's `key` from the dataset (e.g. "express-transport-belt").
 * @param name the English localized name, if the dataset provides one.
 * @param properties remaining scalar fields from the dataset (e.g. "speed"),
 *   kept generic until per-category models exist.
 */
data class Sprite(
    val asset: GameAsset,
    val id: String,
    val name: String,
    val properties: Map<String, String> = emptyMap(),
)
