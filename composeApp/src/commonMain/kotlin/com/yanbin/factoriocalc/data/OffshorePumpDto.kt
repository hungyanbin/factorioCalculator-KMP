package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.OffshorePump
import kotlinx.serialization.Serializable

@Serializable
internal data class OffshorePumpDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val pumpingSpeed: Double,
)

internal fun OffshorePumpDto.toDomain(sheet: SpriteSheetRef): OffshorePump =
    OffshorePump(
        id = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        pumpingSpeed = pumpingSpeed,
    )
