package com.yanbin.factoriocalc.coil

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import coil3.ImageLoader
import coil3.Uri
import coil3.decode.DataSource
import coil3.fetch.FetchResult
import coil3.fetch.Fetcher
import coil3.fetch.ImageFetchResult
import coil3.request.Options
import com.yanbin.factoriocalc.data.ParsedSpriteUri
import com.yanbin.factoriocalc.data.parseSpriteUri

/**
 * Resolves a `sprite://` URI (see [com.yanbin.factoriocalc.data.SpriteUri]) by decoding the
 * shared sheet PNG (cached in [SpriteSheetCache]) and cropping out the requested cell.
 * Coil invokes [fetch] off the main thread, so the decode/crop work never blocks the UI.
 */
internal class SpriteFetcher(private val parsed: ParsedSpriteUri) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val sheet = SpriteSheetCache.get(parsed.sheetBasename)
        return ImageFetchResult(
            image = crop(sheet, parsed).toCoilImage(),
            isSampled = false,
            dataSource = DataSource.DISK,
        )
    }

    private fun crop(sheet: ImageBitmap, parsed: ParsedSpriteUri): ImageBitmap {
        val cell = parsed.cell
        val cropped = ImageBitmap(cell, cell)
        Canvas(cropped).drawImageRect(
            image = sheet,
            srcOffset = IntOffset(parsed.col * cell, parsed.row * cell),
            srcSize = IntSize(cell, cell),
            dstSize = IntSize(cell, cell),
            paint = Paint(),
        )
        return cropped
    }

    class Factory : Fetcher.Factory<Uri> {
        override fun create(data: Uri, options: Options, imageLoader: ImageLoader): Fetcher? {
            val parsed = parseSpriteUri(data) ?: return null
            return SpriteFetcher(parsed)
        }
    }
}
