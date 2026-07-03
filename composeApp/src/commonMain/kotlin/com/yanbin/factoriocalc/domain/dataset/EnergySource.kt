package com.yanbin.factoriocalc.domain.dataset

import kotlinx.serialization.Serializable

/** How a machine draws power — shared by boilers, crafting machines, mining drills, etc. */
@Serializable
data class EnergySource(
    val type: String,
    val fuelCategory: String? = null,
    val emissionsPerMinute: Map<String, Double> = emptyMap(),
)
