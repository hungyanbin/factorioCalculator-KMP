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
        key = id,
        name = name,
        uri = sheet.uriFor(iconCol, iconRow),
        group = group.toItemGroup(),
        subgroup = subgroup.toItemSubgroup(),
        order = order,
        type = type.toItemType(),
        stackSize = stackSize,
    )
