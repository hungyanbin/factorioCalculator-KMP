package com.yanbin.factoriocalc.data

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
        id = id,
        name = name,
        asset = sheet.assetFor(iconCol, iconRow),
        speed = speed,
    )
