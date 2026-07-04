package com.yanbin.factoriocalc.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.yanbin.factoriocalc.data.GameDataRepository
import com.yanbin.factoriocalc.domain.asset.Sprite
import com.yanbin.factoriocalc.domain.dataset.Item
import com.yanbin.factoriocalc.domain.dataset.RawMaterials
import com.yanbin.factoriocalc.domain.dataset.Recipe
import com.yanbin.factoriocalc.domain.dataset.category

@Composable
fun SpriteDetailDialog(
    sprite: Sprite,
    repository: GameDataRepository,
    spriteById: Map<String, Sprite>,
    onDismiss: () -> Unit,
) {
    var recipe by remember(sprite.id) { mutableStateOf<Recipe?>(null) }
    var rawMaterials by remember(sprite.id) { mutableStateOf<RawMaterials?>(null) }

    LaunchedEffect(sprite.id) {
        recipe = repository.getRecipe(sprite.id)
        rawMaterials = repository.getRawMaterials(sprite.id)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.widthIn(max = 420.dp).heightIn(max = 640.dp),
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
        ) {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                DialogHeader(sprite)

                recipe?.let {
                    Section(title = "Recipe") {
                        RecipeRow(it, spriteById)
                    }
                }
                rawMaterials?.let {
                    Section(title = "Total raw") {
                        RawMaterialsRow(it, spriteById)
                    }
                }

                val stackSize = (sprite as? Item)?.stackSize
                val prototypeType = when (sprite) {
                    is Item -> sprite.type.name.lowercase()
                    else -> sprite.category.label
                }
                if (stackSize != null) {
                    InfoRow("Stack size", stackSize.toString())
                }
                InfoRow("Prototype type", prototypeType)
                InfoRow("Internal name", sprite.id)
            }
        }
    }
}

@Composable
private fun DialogHeader(sprite: Sprite) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = sprite.uri,
            contentDescription = sprite.name,
            modifier = Modifier.size(48.dp),
            filterQuality = FilterQuality.Medium,
        )
        Text(
            text = sprite.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
    }
    HorizontalDivider()
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Column(Modifier.padding(top = 8.dp)) { content() }
    }
    HorizontalDivider()
}

@Composable
private fun RecipeRow(recipe: Recipe, spriteById: Map<String, Sprite>) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TimeBadge(recipe.craftingTimeSeconds)
        recipe.ingredients.forEachIndexed { index, ingredient ->
            if (index > 0) Text("+")
            SpriteAmount(ingredient.itemId, ingredient.amount, spriteById)
        }
        Text("→")
        recipe.products.forEach { product ->
            SpriteAmount(product.itemId, product.amount, spriteById)
        }
    }
}

@Composable
private fun RawMaterialsRow(rawMaterials: RawMaterials, spriteById: Map<String, Sprite>) {
    val entries = rawMaterials.materials.entries.sortedByDescending { it.value }
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TimeBadge(rawMaterials.totalTimeSeconds)
        entries.forEach { (itemId, amount) ->
            SpriteAmount(itemId, amount, spriteById)
        }
    }
}

@Composable
private fun TimeBadge(seconds: Double) {
    Text(
        text = "⏱ ${formatAmount(seconds)}",
        style = MaterialTheme.typography.labelLarge,
    )
}

@Composable
private fun SpriteAmount(itemId: String, amount: Double, spriteById: Map<String, Sprite>) {
    val ingredientSprite = spriteById[itemId]
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        if (ingredientSprite != null) {
            AsyncImage(
                model = ingredientSprite.uri,
                contentDescription = ingredientSprite.name,
                modifier = Modifier.size(28.dp),
                filterQuality = FilterQuality.Medium,
            )
        }
        Text(text = "${formatAmount(amount)}${if (ingredientSprite == null) " $itemId" else ""}")
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
    }
    HorizontalDivider()
}

private fun formatAmount(value: Double): String {
    val rounded = kotlin.math.round(value * 10) / 10
    return if (rounded == kotlin.math.floor(rounded)) rounded.toInt().toString() else rounded.toString()
}
