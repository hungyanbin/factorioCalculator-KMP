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
