package com.yanbin.factoriocalc.data

import com.yanbin.factoriocalc.domain.asset.Sprite
import com.yanbin.factoriocalc.domain.dataset.AgriculturalTower
import com.yanbin.factoriocalc.domain.dataset.Belt
import com.yanbin.factoriocalc.domain.dataset.Boiler
import com.yanbin.factoriocalc.domain.dataset.CraftingMachine
import com.yanbin.factoriocalc.domain.dataset.Item
import com.yanbin.factoriocalc.domain.dataset.MiningDrill
import com.yanbin.factoriocalc.domain.dataset.OffshorePump
import com.yanbin.factoriocalc.domain.dataset.Planet
import com.yanbin.factoriocalc.domain.dataset.RawMaterials
import com.yanbin.factoriocalc.domain.dataset.Recipe
import com.yanbin.factoriocalc.domain.dataset.RocketSilo
import com.yanbin.factoriocalc.domain.dataset.computeRawMaterials
import com.yanbin.factoriocalc.resources.Res
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi

private val json = Json { ignoreUnknownKeys = true }

@Serializable
private data class RawDataset(
    val sheet: SpriteSheetRef,
    val belts: List<BeltDto> = emptyList(),
    val boilers: List<BoilerDto> = emptyList(),
    val craftingMachines: List<CraftingMachineDto> = emptyList(),
    val agriculturalTowers: List<AgriculturalTowerDto> = emptyList(),
    val rocketSilos: List<RocketSiloDto> = emptyList(),
    val miningDrills: List<MiningDrillDto> = emptyList(),
    val offshorePumps: List<OffshorePumpDto> = emptyList(),
    val items: List<ItemDto> = emptyList(),
    val planets: List<PlanetDto> = emptyList(),
)

/**
 * Loads the bundled, pre-flattened Factorio sprite dataset from Compose resources.
 * Sprites carry a `sprite://` URI (see [SpriteUri]) that Coil resolves against the
 * sheet PNG directly — no decoded bitmap flows through this repository.
 */
class GameDataRepository {
    private var raw: RawDataset? = null
    private var recipesById: Map<String, Recipe>? = null

    private fun dataset(): RawDataset = checkNotNull(raw) { "call load() first" }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun load() {
        raw ?: json
            .decodeFromString<RawDataset>(Res.readBytes("files/$DATASET_FILE").decodeToString())
            .also { raw = it }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun recipes(): Map<String, Recipe> =
        recipesById ?: json
            .decodeFromString<RecipeFileDto>(Res.readBytes("files/$RECIPE_FILE").decodeToString())
            .recipes
            .associate { it.key to it.toDomain() }
            .also { recipesById = it }

    suspend fun getRecipe(itemId: String): Recipe? = recipes()[itemId]

    suspend fun getRawMaterials(itemId: String, amount: Double = 1.0): RawMaterials? {
        val recipeMap = recipes()
        if (itemId !in recipeMap) return null
        return computeRawMaterials(itemId, amount) { id -> recipeMap[id] }
    }

    fun getBelts(): List<Belt> = dataset().belts.map { it.toDomain(dataset().sheet) }
    fun getBoilers(): List<Boiler> = dataset().boilers.map { it.toDomain(dataset().sheet) }
    fun getCraftingMachines(): List<CraftingMachine> = dataset().craftingMachines.map { it.toDomain(dataset().sheet) }
    fun getAgriculturalTowers(): List<AgriculturalTower> = dataset().agriculturalTowers.map { it.toDomain(dataset().sheet) }
    fun getRocketSilos(): List<RocketSilo> = dataset().rocketSilos.map { it.toDomain(dataset().sheet) }
    fun getMiningDrills(): List<MiningDrill> = dataset().miningDrills.map { it.toDomain(dataset().sheet) }
    fun getOffshorePumps(): List<OffshorePump> = dataset().offshorePumps.map { it.toDomain(dataset().sheet) }
    fun getItems(): List<Item> = dataset().items.map { it.toDomain(dataset().sheet) }
    fun getPlanets(): List<Planet> = dataset().planets.map { it.toDomain(dataset().sheet) }

    /**
     * Every sprite across all categories, for the icon grid. Machines/belts/etc.
     * also appear as generic catalog entries in [getItems] under the same id
     * (e.g. "boiler" is both a [Boiler] and an [Item]) — the more specific
     * category wins so each id contributes exactly one sprite.
     */
    fun getAllSprites(): List<Sprite> {
        val typed = getBelts() + getBoilers() + getCraftingMachines() + getAgriculturalTowers() +
            getRocketSilos() + getMiningDrills() + getOffshorePumps() + getPlanets()
        val typedIds = typed.mapTo(mutableSetOf()) { it.id }
        return typed + getItems().filterNot { it.id in typedIds }
    }

    private companion object {
        const val DATASET_FILE = "sprites-space-age-2.0.55.json"
        const val RECIPE_FILE = "space-age-2.0.55.json"
    }
}
