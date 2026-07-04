package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.EnergySource
import com.yanbin.factoriocalc.domain.dataset.MiningDrill
import kotlinx.serialization.Serializable

@Serializable
internal data class MiningDrillDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val energySource: EnergySource,
    val energyUsage: Double,
    val miningSpeed: Double,
    val moduleSlots: Int,
    val resourceCategories: List<String>,
    val takesFluid: Boolean,
    val allowedEffects: List<String> = emptyList(),
)

internal fun MiningDrillDto.toDomain(sheet: SpriteSheetRef): MiningDrill =
    MiningDrill(
        id = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        energySource = energySource,
        energyUsage = energyUsage,
        miningSpeed = miningSpeed,
        moduleSlots = moduleSlots,
        resourceCategories = resourceCategories,
        takesFluid = takesFluid,
        allowedEffects = allowedEffects,
    )
