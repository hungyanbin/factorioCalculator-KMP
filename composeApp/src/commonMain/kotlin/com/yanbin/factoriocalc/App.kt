package com.yanbin.factoriocalc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yanbin.factoriocalc.domain.asset.AssetRepository
import com.yanbin.factoriocalc.domain.asset.SpriteSheet
import com.yanbin.factoriocalc.ui.SpriteImage

@Composable
fun App() {
    MaterialTheme {
        var sheet by remember { mutableStateOf<SpriteSheet?>(null) }
        var error by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(Unit) {
            try {
                sheet = AssetRepository().load()
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
            else -> IconGrid(data)
        }
    }
}

@Composable
private fun IconGrid(data: SpriteSheet) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 40.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Factorio calculator — ${data.sprites.size} sprites",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            )
        }
        items(data.sprites) { sprite ->
            SpriteImage(
                sprite = sprite,
                sheet = data.image,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}
