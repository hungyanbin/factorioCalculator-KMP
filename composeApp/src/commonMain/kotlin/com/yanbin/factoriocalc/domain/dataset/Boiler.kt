package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.GameAsset
import com.yanbin.factoriocalc.domain.asset.Sprite

data class Boiler(
    override val id: String,
    override val name: String,
    override val asset: GameAsset,
    val energyConsumption: Double,
    val targetTemperature: Double,
    val energySource: EnergySource,
) : Sprite
