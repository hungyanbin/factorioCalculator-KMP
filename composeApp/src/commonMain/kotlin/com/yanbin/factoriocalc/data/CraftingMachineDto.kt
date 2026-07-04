package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.CraftingMachine
import com.yanbin.factoriocalc.domain.dataset.EnergySource
import kotlinx.serialization.Serializable

@Serializable
internal data class CraftingMachineDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val craftingCategories: List<String>,
    val craftingSpeed: Double,
    val energySource: EnergySource,
    val energyUsage: Double,
    val moduleSlots: Int,
    val allowedEffects: List<String> = emptyList(),
    val prodBonus: Double = 0.0,
)

internal fun CraftingMachineDto.toDomain(sheet: SpriteSheetRef): CraftingMachine =
    CraftingMachine(
        id = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        craftingCategories = craftingCategories.map { it.toCraftingCategory() },
        craftingSpeed = craftingSpeed,
        energySource = energySource,
        energyUsage = energyUsage,
        moduleSlots = moduleSlots,
        allowedEffects = allowedEffects,
        prodBonus = prodBonus,
    )
