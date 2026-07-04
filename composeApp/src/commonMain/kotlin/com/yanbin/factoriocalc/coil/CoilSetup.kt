package com.yanbin.factoriocalc.coil

import coil3.ImageLoader
import coil3.SingletonImageLoader

private val installed by lazy {
    SingletonImageLoader.setSafe { context ->
        ImageLoader.Builder(context)
            .components { add(SpriteFetcher.Factory()) }
            .build()
    }
    true
}

/** Installs [SpriteFetcher] as part of Coil's singleton [ImageLoader]. Safe to call more than once. */
internal fun setupCoil() {
    installed
}
