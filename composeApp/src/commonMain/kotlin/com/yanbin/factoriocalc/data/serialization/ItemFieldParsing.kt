package com.yanbin.factoriocalc.data.serialization

import com.yanbin.factoriocalc.domain.dataset.ItemGroup
import com.yanbin.factoriocalc.domain.dataset.ItemSubgroup
import com.yanbin.factoriocalc.domain.dataset.ItemType

internal fun String.toItemGroup(): ItemGroup = when (this) {
    "combat" -> ItemGroup.COMBAT
    "fluids" -> ItemGroup.FLUIDS
    "intermediate-products" -> ItemGroup.INTERMEDIATE_PRODUCTS
    "logistics" -> ItemGroup.LOGISTICS
    "other" -> ItemGroup.OTHER
    "production" -> ItemGroup.PRODUCTION
    "space" -> ItemGroup.SPACE
    else -> error("Unknown item group: $this")
}

internal fun String.toItemSubgroup(): ItemSubgroup = when (this) {
    "agriculture" -> ItemSubgroup.AGRICULTURE
    "agriculture-processes" -> ItemSubgroup.AGRICULTURE_PROCESSES
    "agriculture-products" -> ItemSubgroup.AGRICULTURE_PRODUCTS
    "ammo" -> ItemSubgroup.AMMO
    "aquilo-processes" -> ItemSubgroup.AQUILO_PROCESSES
    "armor" -> ItemSubgroup.ARMOR
    "barrel" -> ItemSubgroup.BARREL
    "belt" -> ItemSubgroup.BELT
    "capsule" -> ItemSubgroup.CAPSULE
    "circuit-network" -> ItemSubgroup.CIRCUIT_NETWORK
    "defensive-structure" -> ItemSubgroup.DEFENSIVE_STRUCTURE
    "energy" -> ItemSubgroup.ENERGY
    "energy-pipe-distribution" -> ItemSubgroup.ENERGY_PIPE_DISTRIBUTION
    "environmental-protection" -> ItemSubgroup.ENVIRONMENTAL_PROTECTION
    "equipment" -> ItemSubgroup.EQUIPMENT
    "extraction-machine" -> ItemSubgroup.EXTRACTION_MACHINE
    "fluid" -> ItemSubgroup.FLUID
    "fulgora-processes" -> ItemSubgroup.FULGORA_PROCESSES
    "gun" -> ItemSubgroup.GUN
    "inserter" -> ItemSubgroup.INSERTER
    "intermediate-product" -> ItemSubgroup.INTERMEDIATE_PRODUCT
    "logistic-network" -> ItemSubgroup.LOGISTIC_NETWORK
    "military-equipment" -> ItemSubgroup.MILITARY_EQUIPMENT
    "module" -> ItemSubgroup.MODULE
    "nauvis-agriculture" -> ItemSubgroup.NAUVIS_AGRICULTURE
    "other" -> ItemSubgroup.OTHER
    "parameters" -> ItemSubgroup.PARAMETERS
    "production-machine" -> ItemSubgroup.PRODUCTION_MACHINE
    "raw-material" -> ItemSubgroup.RAW_MATERIAL
    "raw-resource" -> ItemSubgroup.RAW_RESOURCE
    "science-pack" -> ItemSubgroup.SCIENCE_PACK
    "smelting-machine" -> ItemSubgroup.SMELTING_MACHINE
    "space-interactors" -> ItemSubgroup.SPACE_INTERACTORS
    "space-material" -> ItemSubgroup.SPACE_MATERIAL
    "space-platform" -> ItemSubgroup.SPACE_PLATFORM
    "space-related" -> ItemSubgroup.SPACE_RELATED
    "spawnables" -> ItemSubgroup.SPAWNABLES
    "storage" -> ItemSubgroup.STORAGE
    "terrain" -> ItemSubgroup.TERRAIN
    "tool" -> ItemSubgroup.TOOL
    "train-transport" -> ItemSubgroup.TRAIN_TRANSPORT
    "transport" -> ItemSubgroup.TRANSPORT
    "turret" -> ItemSubgroup.TURRET
    "uranium-processing" -> ItemSubgroup.URANIUM_PROCESSING
    "utility-equipment" -> ItemSubgroup.UTILITY_EQUIPMENT
    "vulcanus-processes" -> ItemSubgroup.VULCANUS_PROCESSES
    else -> error("Unknown item subgroup: $this")
}

internal fun String.toItemType(): ItemType = when (this) {
    "ammo" -> ItemType.AMMO
    "armor" -> ItemType.ARMOR
    "blueprint" -> ItemType.BLUEPRINT
    "blueprint-book" -> ItemType.BLUEPRINT_BOOK
    "capsule" -> ItemType.CAPSULE
    "deconstruction-item" -> ItemType.DECONSTRUCTION_ITEM
    "fluid" -> ItemType.FLUID
    "gun" -> ItemType.GUN
    "item" -> ItemType.ITEM
    "item-with-entity-data" -> ItemType.ITEM_WITH_ENTITY_DATA
    "module" -> ItemType.MODULE
    "rail-planner" -> ItemType.RAIL_PLANNER
    "repair-tool" -> ItemType.REPAIR_TOOL
    "tool" -> ItemType.TOOL
    else -> error("Unknown item type: $this")
}
