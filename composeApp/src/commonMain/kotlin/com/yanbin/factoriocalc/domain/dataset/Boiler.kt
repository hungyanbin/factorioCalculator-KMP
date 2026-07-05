package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class Boiler(
    val key: String,
    override val name: String,
    override val uri: String,
    val energyConsumption: Double,
    val targetTemperature: Double,
    val energySource: EnergySource,
) : Sprite
