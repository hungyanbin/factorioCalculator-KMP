package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class CraftingMachine(
    val key: String,
    override val name: String,
    override val uri: String,
    val craftingCategories: List<CraftingCategory>,
    val craftingSpeed: Double,
    val energySource: EnergySource,
    val energyUsage: Double,
    val moduleSlots: Int,
    val allowedEffects: List<String>,
    val prodBonus: Double,
) : Sprite
