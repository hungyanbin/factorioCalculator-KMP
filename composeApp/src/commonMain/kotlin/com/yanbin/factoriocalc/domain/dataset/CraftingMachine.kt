package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.GameAsset
import com.yanbin.factoriocalc.domain.asset.Sprite

data class CraftingMachine(
    override val id: String,
    override val name: String,
    override val asset: GameAsset,
    val craftingCategories: List<String>,
    val craftingSpeed: Double,
    val energySource: EnergySource,
    val energyUsage: Double,
    val moduleSlots: Int,
    val allowedEffects: List<String>,
    val prodBonus: Double,
) : Sprite
