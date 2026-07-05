package com.yanbin.factoriocalc.data.serialization

import com.yanbin.factoriocalc.domain.dataset.Belt
import kotlinx.serialization.Serializable

@Serializable
internal data class BeltDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val speed: Double,
)

internal fun BeltDto.toDomain(sheet: SpriteSheetRef): Belt =
    Belt(
        key = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        speed = speed,
    )
