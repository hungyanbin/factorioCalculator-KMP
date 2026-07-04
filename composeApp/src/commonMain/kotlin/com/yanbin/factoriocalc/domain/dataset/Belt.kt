package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class Belt(
    override val id: String,
    override val name: String,
    override val uri: String,
    val speed: Double,
) : Sprite
