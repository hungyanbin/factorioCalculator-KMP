package com.yanbin.factoriocalc.data

import androidx.compose.ui.graphics.ImageBitmap
import com.yanbin.factoriocalc.domain.asset.Sprite
import com.yanbin.factoriocalc.domain.dataset.AgriculturalTower
import com.yanbin.factoriocalc.domain.dataset.Belt
import com.yanbin.factoriocalc.domain.dataset.Boiler
import com.yanbin.factoriocalc.domain.dataset.CraftingMachine
import com.yanbin.factoriocalc.domain.dataset.Item
import com.yanbin.factoriocalc.domain.dataset.MiningDrill
import com.yanbin.factoriocalc.domain.dataset.OffshorePump
import com.yanbin.factoriocalc.domain.dataset.Planet
import com.yanbin.factoriocalc.domain.dataset.RocketSilo
import com.yanbin.factoriocalc.resources.Res
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.decodeToImageBitmap

/** The decoded sprite sheet plus every sprite drawn from it, for the icon grid. */
data class SpriteSheet(
    val image: ImageBitmap,
    val sprites: List<Sprite>,
)

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
 * Loads the bundled, pre-flattened Factorio sprite dataset + its sprite sheet
 * from Compose resources. The sheet is decoded once into a single [ImageBitmap];
 * individual sprites are drawn as sub-rects of it (see the UI layer).
 */
class GameDataRepository {
    private var raw: RawDataset? = null
    private var image: ImageBitmap? = null

    private fun dataset(): RawDataset = checkNotNull(raw) { "call load() first" }

    @OptIn(ExperimentalResourceApi::class)
    suspend fun load(): SpriteSheet {
        val dataset = raw ?: json
            .decodeFromString<RawDataset>(Res.readBytes("files/$DATASET_FILE").decodeToString())
            .also { raw = it }
        val bitmap = image ?: Res.readBytes("files/$SHEET_FILE").decodeToImageBitmap().also { image = it }

        return SpriteSheet(image = bitmap, sprites = getAllSprites())
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
        const val SHEET_FILE = "sprite-sheet-1d2b6676016208d82ff2a56043bb3e4d.png"
    }
}
