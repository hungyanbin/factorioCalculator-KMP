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
import com.yanbin.factoriocalc.domain.dataset.prototypeType

@Composable
fun SpriteDetailDialog(
    sprite: Sprite,
    repository: GameDataRepository,
    itemsByKey: Map<String, Item>,
    onDismiss: () -> Unit,
) {
    var recipe by remember(sprite) { mutableStateOf<Recipe?>(null) }
    var rawMaterials by remember(sprite) { mutableStateOf<RawMaterials?>(null) }

    LaunchedEffect(sprite) {
        val item = sprite as? Item
        recipe = item?.let { repository.getRecipe(it.key) }
        rawMaterials = item?.let { repository.getRawMaterials(it.key) }
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
                        RecipeRow(it, itemsByKey)
                    }
                }
                rawMaterials?.let {
                    Section(title = "Total raw") {
                        RawMaterialsRow(it, itemsByKey)
                    }
                }

                if (sprite is Item) {
                    if (sprite.stackSize != null) {
                        InfoRow("Stack size", sprite.stackSize.toString())
                    }
                    InfoRow("Prototype type", sprite.prototypeType)
                    InfoRow("Internal name", sprite.key)
                } else {
                    InfoRow("Prototype type", sprite.prototypeType)
                }
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
private fun RecipeRow(recipe: Recipe, itemsByKey: Map<String, Item>) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TimeBadge(recipe.craftingTimeSeconds)
        recipe.ingredients.forEachIndexed { index, ingredient ->
            if (index > 0) Text("+")
            SpriteAmount(ingredient.itemId, ingredient.amount, itemsByKey)
        }
        Text("→")
        recipe.products.forEach { product ->
            SpriteAmount(product.itemId, product.amount, itemsByKey)
        }
    }
}

@Composable
private fun RawMaterialsRow(rawMaterials: RawMaterials, itemsByKey: Map<String, Item>) {
    val entries = rawMaterials.materials.entries.sortedByDescending { it.value }
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TimeBadge(rawMaterials.totalTimeSeconds)
        entries.forEach { (itemId, amount) ->
            SpriteAmount(itemId, amount, itemsByKey)
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
private fun SpriteAmount(itemId: String, amount: Double, itemsByKey: Map<String, Item>) {
    val ingredientItem = itemsByKey[itemId]
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        if (ingredientItem != null) {
            AsyncImage(
                model = ingredientItem.uri,
                contentDescription = ingredientItem.name,
                modifier = Modifier.size(28.dp),
                filterQuality = FilterQuality.Medium,
            )
        }
        Text(text = "${formatAmount(amount)}${if (ingredientItem == null) " $itemId" else ""}")
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
