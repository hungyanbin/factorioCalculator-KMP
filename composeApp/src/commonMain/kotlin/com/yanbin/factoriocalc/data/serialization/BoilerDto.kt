package com.yanbin.factoriocalc.data.serialization

import com.yanbin.factoriocalc.domain.dataset.Boiler
import com.yanbin.factoriocalc.domain.dataset.EnergySource
import kotlinx.serialization.Serializable

@Serializable
internal data class BoilerDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val energyConsumption: Double,
    val targetTemperature: Double,
    val energySource: EnergySource,
)

internal fun BoilerDto.toDomain(sheet: SpriteSheetRef): Boiler =
    Boiler(
        key = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        energyConsumption = energyConsumption,
        targetTemperature = targetTemperature,
        energySource = energySource,
    )
