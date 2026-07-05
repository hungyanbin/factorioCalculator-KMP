package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.RocketSilo
import kotlinx.serialization.Serializable

@Serializable
internal data class RocketSiloDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val craftingCategories: List<String>,
    val craftingSpeed: Double,
    val energyUsage: Double,
    val moduleSlots: Int,
    val allowedEffects: List<String> = emptyList(),
)

internal fun RocketSiloDto.toDomain(sheet: SpriteSheetRef): RocketSilo =
    RocketSilo(
        key = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        craftingCategories = craftingCategories.map { it.toCraftingCategory() },
        craftingSpeed = craftingSpeed,
        energyUsage = energyUsage,
        moduleSlots = moduleSlots,
        allowedEffects = allowedEffects,
    )
