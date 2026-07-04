package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.Item
import kotlinx.serialization.Serializable

@Serializable
internal data class ItemDto(
    val id: String,
    val name: String,
    val iconCol: Int,
    val iconRow: Int,
    val group: String,
    val subgroup: String,
    val order: String,
    val type: String,
    val stackSize: Int? = null,
)

internal fun ItemDto.toDomain(sheet: SpriteSheetRef): Item =
    Item(
        id = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        group = group,
        subgroup = subgroup,
        order = order,
        type = type,
        stackSize = stackSize,
    )
