package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class RocketSilo(
    override val id: String,
    override val name: String,
    override val uri: String,
    val craftingCategories: List<String>,
    val craftingSpeed: Double,
    val energyUsage: Double,
    val moduleSlots: Int,
    val allowedEffects: List<String>,
) : Sprite
