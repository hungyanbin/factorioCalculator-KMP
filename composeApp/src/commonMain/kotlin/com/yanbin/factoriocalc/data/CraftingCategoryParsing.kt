package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.dataset.CraftingCategory

internal fun String.toCraftingCategory(): CraftingCategory = when (this) {
    "advanced-crafting" -> CraftingCategory.ADVANCED_CRAFTING
    "basic-crafting" -> CraftingCategory.BASIC_CRAFTING
    "captive-spawner-process" -> CraftingCategory.CAPTIVE_SPAWNER_PROCESS
    "centrifuging" -> CraftingCategory.CENTRIFUGING
    "chemistry" -> CraftingCategory.CHEMISTRY
    "chemistry-or-cryogenics" -> CraftingCategory.CHEMISTRY_OR_CRYOGENICS
    "crafting" -> CraftingCategory.CRAFTING
    "crafting-with-fluid" -> CraftingCategory.CRAFTING_WITH_FLUID
    "crafting-with-fluid-or-metallurgy" -> CraftingCategory.CRAFTING_WITH_FLUID_OR_METALLURGY
    "crushing" -> CraftingCategory.CRUSHING
    "cryogenics" -> CraftingCategory.CRYOGENICS
    "cryogenics-or-assembling" -> CraftingCategory.CRYOGENICS_OR_ASSEMBLING
    "electromagnetics" -> CraftingCategory.ELECTROMAGNETICS
    "electronics" -> CraftingCategory.ELECTRONICS
    "electronics-or-assembling" -> CraftingCategory.ELECTRONICS_OR_ASSEMBLING
    "electronics-with-fluid" -> CraftingCategory.ELECTRONICS_WITH_FLUID
    "metallurgy" -> CraftingCategory.METALLURGY
    "metallurgy-or-assembling" -> CraftingCategory.METALLURGY_OR_ASSEMBLING
    "oil-processing" -> CraftingCategory.OIL_PROCESSING
    "organic" -> CraftingCategory.ORGANIC
    "organic-or-assembling" -> CraftingCategory.ORGANIC_OR_ASSEMBLING
    "organic-or-chemistry" -> CraftingCategory.ORGANIC_OR_CHEMISTRY
    "organic-or-hand-crafting" -> CraftingCategory.ORGANIC_OR_HAND_CRAFTING
    "pressing" -> CraftingCategory.PRESSING
    "recycling" -> CraftingCategory.RECYCLING
    "recycling-or-hand-crafting" -> CraftingCategory.RECYCLING_OR_HAND_CRAFTING
    "rocket-building" -> CraftingCategory.ROCKET_BUILDING
    "smelting" -> CraftingCategory.SMELTING
    else -> error("Unknown crafting category: $this")
}
