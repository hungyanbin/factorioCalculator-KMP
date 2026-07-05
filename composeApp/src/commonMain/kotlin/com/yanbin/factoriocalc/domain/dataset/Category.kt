package com.yanbin.factoriocalc.domain.dataset

import com.yanbin.factoriocalc.domain.asset.Sprite

enum class Category(val label: String) {
    BELT("Belt"),
    BOILER("Boiler"),
    CRAFTING_MACHINE("Crafting Machine"),
    AGRICULTURAL_TOWER("Agricultural Tower"),
    ROCKET_SILO("Rocket Silo"),
    MINING_DRILL("Mining Drill"),
    OFFSHORE_PUMP("Offshore Pump"),
    ITEM("Item"),
    PLANET("Planet"),
}

val Sprite.category: Category
    get() = when (this) {
        is Belt -> Category.BELT
        is Boiler -> Category.BOILER
        is CraftingMachine -> Category.CRAFTING_MACHINE
        is AgriculturalTower -> Category.AGRICULTURAL_TOWER
        is RocketSilo -> Category.ROCKET_SILO
        is MiningDrill -> Category.MINING_DRILL
        is OffshorePump -> Category.OFFSHORE_PUMP
        is Item -> Category.ITEM
        is Planet -> Category.PLANET
        else -> error("Unknown sprite type: ${this::class}")
    }

/**
 * Identity unique across every sprite, including two sprites for the same real-world object in
 * different categories (e.g. the [Belt] and [Item] both named "transport-belt"). Each concrete
 * type's own `key` is only unique within its own category, so [category] disambiguates the rest.
 */
val Sprite.uniqueKey: String
    get() = when (this) {
        is Belt -> "${Category.BELT}:$key"
        is Boiler -> "${Category.BOILER}:$key"
        is CraftingMachine -> "${Category.CRAFTING_MACHINE}:$key"
        is AgriculturalTower -> "${Category.AGRICULTURAL_TOWER}:$key"
        is RocketSilo -> "${Category.ROCKET_SILO}:$key"
        is MiningDrill -> "${Category.MINING_DRILL}:$key"
        is OffshorePump -> "${Category.OFFSHORE_PUMP}:$key"
        is Item -> "${Category.ITEM}:$key"
        is Planet -> "${Category.PLANET}:$key"
        else -> error("Unknown sprite type: ${this::class}")
    }
