package com.yanbin.factoriocalc.coil

import androidx.compose.ui.graphics.ImageBitmap
import com.yanbin.factoriocalc.resources.Res
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

/**
 * Every sprite shares one physical sheet PNG; this decodes it at most once so that
 * many concurrent [SpriteFetcher] requests (e.g. the whole icon grid on first render)
 * don't each independently decode the same ~1.5MB file.
 */
internal object SpriteSheetCache {
    private val mutex = Mutex()
    private var cached: Pair<String, ImageBitmap>? = null

    @OptIn(ExperimentalResourceApi::class)
    suspend fun get(basename: String): ImageBitmap = mutex.withLock {
        cached?.takeIf { it.first == basename }?.second
            ?: Res.readBytes("files/$basename").decodeToImageBitmap().also { cached = basename to it }
    }
}
