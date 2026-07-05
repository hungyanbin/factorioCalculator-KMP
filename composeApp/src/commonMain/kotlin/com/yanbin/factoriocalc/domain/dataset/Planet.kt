package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite
import kotlinx.serialization.Serializable

@Serializable
data class PlanetResources(
    val offshore: List<String> = emptyList(),
    val plants: List<String> = emptyList(),
    val resource: List<String> = emptyList(),
)

data class Planet(
    val key: String,
    override val name: String,
    override val uri: String,
    val order: String,
    val resources: PlanetResources,
    val surfaceProperties: Map<String, Double>,
) : Sprite
