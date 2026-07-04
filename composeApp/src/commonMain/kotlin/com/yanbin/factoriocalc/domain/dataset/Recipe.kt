package com.yanbin.factoriocalc.domain.dataset

data class RecipeIngredient(
    val itemId: String,
    val amount: Double,
)

data class Recipe(
    val id: String,
    val category: CraftingCategory,
    val craftingTimeSeconds: Double,
    val ingredients: List<RecipeIngredient>,
    val products: List<RecipeIngredient>,
)
