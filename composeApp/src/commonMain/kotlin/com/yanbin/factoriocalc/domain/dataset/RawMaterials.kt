package com.yanbin.factoriocalc.domain.dataset

data class RawMaterials(
    val materials: Map<String, Double>,
    val totalTimeSeconds: Double,
)

/**
 * Recursively expands [itemId]'s recipe tree via [recipeFor] into the raw materials and total
 * crafting time needed to produce [amount] of it. An ingredient with no recipe is raw (a leaf).
 */
fun computeRawMaterials(
    itemId: String,
    amount: Double,
    recipeFor: (String) -> Recipe?,
): RawMaterials {
    val materials = mutableMapOf<String, Double>()
    var totalTime = 0.0

    fun expand(id: String, needed: Double, visited: Set<String>) {
        val recipe = recipeFor(id)
        if (recipe == null || id in visited) {
            materials[id] = (materials[id] ?: 0.0) + needed
            return
        }
        val producedPerCraft = recipe.products.firstOrNull { it.itemId == id }?.amount ?: return
        val crafts = needed / producedPerCraft
        totalTime += recipe.craftingTimeSeconds * crafts
        val nextVisited = visited + id
        recipe.ingredients.forEach { ingredient ->
            expand(ingredient.itemId, ingredient.amount * crafts, nextVisited)
        }
    }

    expand(itemId, amount, emptySet())
    return RawMaterials(materials, totalTime)
}
