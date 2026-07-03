package com.yanbin.factoriocalc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.yanbin.factoriocalc.data.GameDataRepository
import com.yanbin.factoriocalc.data.SpriteSheet
import com.yanbin.factoriocalc.domain.asset.Sprite
import com.yanbin.factoriocalc.domain.dataset.Category
import com.yanbin.factoriocalc.domain.dataset.category
import com.yanbin.factoriocalc.ui.SpriteImage

@Composable
fun App() {
    MaterialTheme {
        var sheet by remember { mutableStateOf<SpriteSheet?>(null) }
        var error by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(Unit) {
            try {
                sheet = GameDataRepository().load()
            } catch (t: Throwable) {
                error = "${t::class.simpleName}: ${t.message}"
            }
        }

        val data = sheet
        val err = error
        when {
            err != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Load failed — $err", textAlign = TextAlign.Center)
            }
            data == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            else -> SpriteBrowser(data)
        }
    }
}

@Composable
private fun SpriteBrowser(data: SpriteSheet) {
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    val sprites = remember(data.sprites, selectedCategory) {
        val category = selectedCategory
        if (category == null) data.sprites else data.sprites.filter { it.category == category }
    }

    Column(Modifier.fillMaxSize()) {
        CategoryDropdown(
            selected = selectedCategory,
            onSelect = { selectedCategory = it },
            modifier = Modifier.padding(16.dp),
        )
        IconGrid(image = data.image, sprites = sprites)
    }
}

@Composable
private fun CategoryDropdown(
    selected: Category?,
    onSelect: (Category?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier) {
        OutlinedButton(onClick = { expanded = true }) {
            Text((selected?.label ?: "All") + " ▾")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("All") },
                onClick = { onSelect(null); expanded = false },
            )
            Category.entries.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.label) },
                    onClick = { onSelect(category); expanded = false },
                )
            }
        }
    }
}

@Composable
private fun IconGrid(image: ImageBitmap, sprites: List<Sprite>) {
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
        items(sprites, key = { sprite -> sprite.id }) { sprite ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(4.dp),
            ) {
                SpriteImage(
                    sprite = sprite,
                    sheet = image,
                    modifier = Modifier.size(48.dp),
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
