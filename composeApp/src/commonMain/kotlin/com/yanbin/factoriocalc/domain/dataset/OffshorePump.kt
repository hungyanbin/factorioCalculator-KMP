package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class OffshorePump(
    val key: String,
    override val name: String,
    override val uri: String,
    val pumpingSpeed: Double,
) : Sprite
