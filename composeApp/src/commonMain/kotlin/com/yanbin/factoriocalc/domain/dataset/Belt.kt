package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class Belt(
    val key: String,
    override val name: String,
    override val uri: String,
    val speed: Double,
) : Sprite
