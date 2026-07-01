package com.yanbin.factoriocalc.domain.asset

import androidx.compose.ui.graphics.ImageBitmap
import com.yanbin.factoriocalc.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

/** The decoded sprite sheet plus the sprites that index into it. */
data class SpriteSheet(
    val image: ImageBitmap,
    val sprites: List<Sprite>,
)

/**
 * Loads the bundled Factorio dataset + its sprite sheet from Compose resources.
 * The sheet is decoded once into a single [ImageBitmap]; individual sprites are
 * drawn as sub-rects of it (see the UI layer).
 */
class AssetRepository(
    private val loader: SpriteLoader = SpriteLoader(),
) {
    private val spritesCache = mutableListOf<Sprite>()

    @OptIn(ExperimentalResourceApi::class)
    suspend fun load(): SpriteSheet {
        if (spritesCache.isEmpty()) {
            val json = Res.readBytes("files/$DATASET_FILE").decodeToString()
            spritesCache += loader.parse(json)
        }

        val image = Res.readBytes("files/$SHEET_FILE").decodeToImageBitmap()
        return SpriteSheet(image = image, sprites = spritesCache)
    }

    private companion object {
        const val DATASET_FILE = "vanilla-2.0.55.json"
        const val SHEET_FILE = "sprite-sheet-df91c1c1283939c08e7af8b006ed2f09.png"
    }
}
