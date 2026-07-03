package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.GameAsset
import com.yanbin.factoriocalc.domain.asset.Sprite

data class Item(
    override val id: String,
    override val name: String,
    override val asset: GameAsset,
    val group: String,
    val subgroup: String,
    val order: String,
    val type: String,
    val stackSize: Int?,
) : Sprite
