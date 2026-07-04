package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class AgriculturalTower(
    override val id: String,
    override val name: String,
    override val uri: String,
    val energySource: EnergySource,
    val energyUsage: Double,
) : Sprite
