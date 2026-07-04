package com.yanbin.factoriocalc.coil

import androidx.compose.ui.graphics.ImageBitmap
import coil3.Image

/** Bridges a Compose [ImageBitmap] into Coil's platform-agnostic [Image]. */
internal expect fun ImageBitmap.toCoilImage(): Image
