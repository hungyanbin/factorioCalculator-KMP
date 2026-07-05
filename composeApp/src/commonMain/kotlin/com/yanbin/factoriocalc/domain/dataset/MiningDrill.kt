package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class MiningDrill(
    val key: String,
    override val name: String,
    override val uri: String,
    val energySource: EnergySource,
    val energyUsage: Double,
    val miningSpeed: Double,
    val moduleSlots: Int,
    val resourceCategories: List<String>,
    val takesFluid: Boolean,
    val allowedEffects: List<String>,
) : Sprite
