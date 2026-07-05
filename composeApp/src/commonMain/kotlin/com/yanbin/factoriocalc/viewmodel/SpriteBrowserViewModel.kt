package com.yanbin.factoriocalc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yanbin.factoriocalc.data.GameDataRepository
import com.yanbin.factoriocalc.domain.asset.Sprite
import com.yanbin.factoriocalc.domain.dataset.Category
import com.yanbin.factoriocalc.domain.dataset.Item
import com.yanbin.factoriocalc.domain.dataset.ItemGroup
import com.yanbin.factoriocalc.domain.dataset.RawMaterials
import com.yanbin.factoriocalc.domain.dataset.Recipe
import com.yanbin.factoriocalc.domain.dataset.category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SpriteBrowserViewModel(
    private val repository: GameDataRepository,
) : ViewModel() {

    val isLoading: StateFlow<Boolean>
        field = MutableStateFlow(true)

    val error: StateFlow<String?>
        field = MutableStateFlow<String?>(null)

    private val _allSprites = MutableStateFlow<List<Sprite>>(emptyList())

    val itemsByKey: StateFlow<Map<String, Item>> = _allSprites
        .map { sprites -> sprites.filterIsInstance<Item>().associateBy { it.key } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    val selectedCategory: StateFlow<Category?>
        field = MutableStateFlow<Category?>(null)

    val categoryOptions: StateFlow<List<Category?>> = _allSprites
        .map { sprites -> listOf(null) + sprites.map { it.category }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf(null))

    val showGroupDropdown: StateFlow<Boolean> = selectedCategory
        .map { it == Category.ITEM }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val selectedGroup: StateFlow<ItemGroup?>
        field = MutableStateFlow<ItemGroup?>(null)

    val groupOptions: StateFlow<List<ItemGroup?>> = combine(
        _allSprites,
        selectedCategory
    ) { sprites, category ->
        if (category == Category.ITEM) {
            listOf(null) + sprites.filterIsInstance<Item>().map { it.group }.distinct().sorted()
        } else {
            listOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf(null))

    val visibleSprites: StateFlow<List<Sprite>> = combine(
        _allSprites, selectedCategory, selectedGroup,
    ) { sprites, category, group ->
        val byCategory =
            if (category == null) sprites else sprites.filter { it.category == category }
        if (category == Category.ITEM && group != null) {
            byCategory.filter { it is Item && it.group == group }
        } else {
            byCategory
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val selectedSprite: StateFlow<Sprite?>
        field = MutableStateFlow<Sprite?>(null)

    val recipe: StateFlow<Recipe?>
        field = MutableStateFlow<Recipe?>(null)

    val rawMaterials: StateFlow<RawMaterials?>
        field = MutableStateFlow<RawMaterials?>(null)

    init {
        viewModelScope.launch {
            try {
                repository.load()
                _allSprites.value = repository.getAllSprites()
            } catch (t: Throwable) {
                error.value = "${t::class.simpleName}: ${t.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun onCategorySelected(category: Category?) {
        selectedCategory.value = category
        selectedGroup.value = null
    }

    fun onGroupSelected(group: ItemGroup?) {
        selectedGroup.value = group
    }

    fun onSpriteClick(sprite: Sprite) {
        selectedSprite.value = sprite
        recipe.value = null
        rawMaterials.value = null
        viewModelScope.launch {
            val item = sprite as? Item
            val fetchedRecipe = item?.let { repository.getRecipe(it.key) }
            val fetchedRawMaterials = item?.let { repository.getRawMaterials(it.key) }
            if (selectedSprite.value == sprite) {
                recipe.value = fetchedRecipe
                rawMaterials.value = fetchedRawMaterials
            }
        }
    }

    fun onDialogDismiss() {
        selectedSprite.value = null
        recipe.value = null
        rawMaterials.value = null
    }
}