package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.Planet
import com.yanbin.factoriocalc.domain.dataset.PlanetResources
import kotlinx.serialization.Serializable

@Serializable
internal data class PlanetDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val order: String,
    val resources: PlanetResources,
    val surfaceProperties: Map<String, Double> = emptyMap(),
)

internal fun PlanetDto.toDomain(sheet: SpriteSheetRef): Planet =
    Planet(
        id = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        order = order,
        resources = resources,
        surfaceProperties = surfaceProperties,
    )
