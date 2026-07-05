package com.yanbin.factoriocalc.data.serialization

import com.yanbin.factoriocalc.domain.dataset.Recipe
import com.yanbin.factoriocalc.domain.dataset.RecipeIngredient
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RecipeIngredientDto(
    val name: String,
    val amount: Double,
)

@Serializable
internal data class RecipeDto(
    val key: String,
    val category: String,
    @SerialName("energy_required") val energyRequired: Double,
    val ingredients: List<RecipeIngredientDto> = emptyList(),
    val results: List<RecipeIngredientDto> = emptyList(),
)

@Serializable
internal data class RecipeFileDto(
    val recipes: List<RecipeDto> = emptyList(),
)

internal fun RecipeDto.toDomain(): Recipe =
    Recipe(
        id = key,
        category = category.toCraftingCategory(),
        craftingTimeSeconds = energyRequired,
        ingredients = ingredients.map { RecipeIngredient(it.name, it.amount) },
        products = results.map { RecipeIngredient(it.name, it.amount) },
    )
