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
import androidx.compose.runtime.LaunchedEffect
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
import coil3.compose.AsyncImage
import com.yanbin.factoriocalc.coil.setupCoil
import com.yanbin.factoriocalc.data.GameDataRepository
import com.yanbin.factoriocalc.domain.asset.Sprite
import com.yanbin.factoriocalc.domain.dataset.Category
import com.yanbin.factoriocalc.domain.dataset.Item
import com.yanbin.factoriocalc.domain.dataset.ItemGroup
import com.yanbin.factoriocalc.domain.dataset.category
import com.yanbin.factoriocalc.domain.dataset.uniqueKey
import com.yanbin.factoriocalc.ui.SpriteDetailDialog

@Composable
fun App() {
    setupCoil()

    MaterialTheme {
        var sprites by remember { mutableStateOf<List<Sprite>?>(null) }
        var error by remember { mutableStateOf<String?>(null) }
        val repository = remember { GameDataRepository() }
        LaunchedEffect(Unit) {
            try {
                repository.load()
                sprites = repository.getAllSprites()
            } catch (t: Throwable) {
                error = "${t::class.simpleName}: ${t.message}"
            }
        }

        val data = sprites
        val err = error
        when {
            err != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Load failed — $err", textAlign = TextAlign.Center)
            }
            data == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            else -> SpriteBrowser(data, repository)
        }
    }
}

@Composable
private fun SpriteBrowser(allSprites: List<Sprite>, repository: GameDataRepository) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedGroup by remember { mutableStateOf<ItemGroup?>(null) }
    val categories = remember(allSprites) { allSprites.map { it.category }.distinct().sorted() }
    val groups = remember(allSprites) {
        allSprites.filterIsInstance<Item>().map { it.group }.distinct().sorted()
    }
    val sprites = remember(allSprites, selectedCategory, selectedGroup) {
        val category = selectedCategory
        val byCategory = if (category == null) allSprites else allSprites.filter { it.category == category }
        val group = selectedGroup
        if (category == Category.ITEM && group != null) {
            byCategory.filter { it is Item && it.group == group }
        } else {
            byCategory
        }
    }
    val itemsByKey = remember(allSprites) { allSprites.filterIsInstance<Item>().associateBy { it.key } }
    var selectedSprite by remember { mutableStateOf<Sprite?>(null) }

    Column(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LabeledDropdown(
                label = "Category",
                options = categories,
                selected = selectedCategory,
                optionLabel = { it.label },
                onSelect = { selectedCategory = it; selectedGroup = null },
            )
            if (selectedCategory == Category.ITEM) {
                LabeledDropdown(
                    label = "Group",
                    options = groups,
                    selected = selectedGroup,
                    optionLabel = { it.label },
                    onSelect = { selectedGroup = it },
                )
            }
        }
        IconGrid(sprites = sprites, onSpriteClick = { selectedSprite = it })
    }

    selectedSprite?.let { sprite ->
        SpriteDetailDialog(
            sprite = sprite,
            repository = repository,
            itemsByKey = itemsByKey,
            onDismiss = { selectedSprite = null },
        )
    }
}

@Composable
private fun <T> LabeledDropdown(
    label: String,
    options: List<T>,
    selected: T?,
    optionLabel: (T) -> String,
    onSelect: (T?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text("$label: ")
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text((selected?.let(optionLabel) ?: "All") + " ▾")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("All") },
                    onClick = { onSelect(null); expanded = false },
                )
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
