package com.yanbin.factoriocalc

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.yanbin.factoriocalc.coil.setupCoil
import com.yanbin.factoriocalc.data.GameDataRepository
import com.yanbin.factoriocalc.domain.asset.Sprite
import com.yanbin.factoriocalc.domain.dataset.uniqueKey
import com.yanbin.factoriocalc.viewmodel.SpriteBrowserViewModel
import com.yanbin.factoriocalc.ui.SpriteDetailDialog

@Composable
fun App() {
    setupCoil()

    MaterialTheme {
        val viewModel = viewModel { SpriteBrowserViewModel(GameDataRepository()) }
        val isLoading by viewModel.isLoading.collectAsState()
        val error by viewModel.error.collectAsState()

        when {
            error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Load failed — $error", textAlign = TextAlign.Center)
            }
            isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            else -> SpriteBrowser(viewModel)
        }
    }
}

@Composable
private fun SpriteBrowser(viewModel: SpriteBrowserViewModel) {
    val categoryOptions by viewModel.categoryOptions.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val showGroupDropdown by viewModel.showGroupDropdown.collectAsState()
    val groupOptions by viewModel.groupOptions.collectAsState()
    val selectedGroup by viewModel.selectedGroup.collectAsState()
    val visibleSprites by viewModel.visibleSprites.collectAsState()
    val itemsByKey by viewModel.itemsByKey.collectAsState()
    val selectedSprite by viewModel.selectedSprite.collectAsState()
    val recipe by viewModel.recipe.collectAsState()
    val rawMaterials by viewModel.rawMaterials.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LabeledDropdown(
                label = "Category",
                options = categoryOptions,
                selected = selectedCategory,
                optionLabel = { it?.label ?: "All" },
                onSelect = viewModel::onCategorySelected,
            )
            if (showGroupDropdown) {
                LabeledDropdown(
                    label = "Group",
                    options = groupOptions,
                    selected = selectedGroup,
                    optionLabel = { it?.label ?: "All" },
                    onSelect = viewModel::onGroupSelected,
                )
            }
        }
        IconGrid(sprites = visibleSprites, onSpriteClick = viewModel::onSpriteClick)
    }

    selectedSprite?.let { sprite ->
        SpriteDetailDialog(
            sprite = sprite,
            recipe = recipe,
            rawMaterials = rawMaterials,
            itemsByKey = itemsByKey,
            onDismiss = viewModel::onDialogDismiss,
        )
    }
}

@Composable
private fun <T> LabeledDropdown(
    label: String,
    options: List<T>,
    selected: T,
    optionLabel: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text("$label: ")
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(optionLabel(selected) + " ▾")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(optionLabel(option)) },
                        onClick = { onSelect(option); expanded = false },
                    )
                }
            }
        }
    }
}

@Composable
private fun IconGrid(sprites: List<Sprite>, onSpriteClick: (Sprite) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 96.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Factorio calculator — ${sprites.size} sprites",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            )
        }
        items(sprites, key = { sprite -> sprite.uniqueKey }) { sprite ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp).clickable { onSpriteClick(sprite) },
            ) {
                AsyncImage(
                    model = sprite.uri,
                    contentDescription = sprite.name,
                    modifier = Modifier.size(48.dp),
                    filterQuality = FilterQuality.Medium,
                )
                Text(
                    text = sprite.name,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}
