package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.AgriculturalTower
import com.yanbin.factoriocalc.domain.dataset.EnergySource
import kotlinx.serialization.Serializable

@Serializable
internal data class AgriculturalTowerDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val energySource: EnergySource,
    val energyUsage: Double,
)

internal fun AgriculturalTowerDto.toDomain(sheet: SpriteSheetRef): AgriculturalTower =
    AgriculturalTower(
        id = id,
        name = name,
        asset = sheet.assetFor(iconCol, iconRow),
        energySource = energySource,
        energyUsage = energyUsage,
    )
