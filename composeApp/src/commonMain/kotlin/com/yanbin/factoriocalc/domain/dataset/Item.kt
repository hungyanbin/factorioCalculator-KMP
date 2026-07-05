package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

data class Item(
    val key: String,
    override val name: String,
    override val uri: String,
    val group: ItemGroup,
    val subgroup: ItemSubgroup,
    val order: String,
    val type: ItemType,
    val stackSize: Int?,
) : Sprite
