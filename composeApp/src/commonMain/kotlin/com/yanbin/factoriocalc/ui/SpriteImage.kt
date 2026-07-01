package com.yanbin.factoriocalc.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import com.yanbin.factoriocalc.domain.asset.Sprite
import kotlin.math.roundToInt

/**
 * Draws a single [sprite] by blitting its sub-rectangle out of the shared
 * [sheet] — the Compose equivalent of the reference site's CSS
 * `background-position`. No per-icon bitmap is created.
 */
@Composable
fun SpriteImage(
    sprite: Sprite,
    sheet: ImageBitmap,
    modifier: Modifier = Modifier,
) {
    val srcOffset = IntOffset(sprite.asset.offset.x, sprite.asset.offset.y)
    val srcSize = IntSize(sprite.asset.size.width, sprite.asset.size.height)
    Canvas(modifier) {
        drawImage(
            image = sheet,
            srcOffset = srcOffset,
            srcSize = srcSize,
            dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt()),
            // Smooth (bilinear) scaling: uniform at any device-pixel ratio.
            // FilterQuality.None (nearest) unevenly duplicates pixels at
            // fractional DPI, which looks stretched/squished.
            filterQuality = FilterQuality.Medium,
        )
    }
}
