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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SpriteBrowserViewModel(
    private val repository: GameDataRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _allSprites = MutableStateFlow<List<Sprite>>(emptyList())

    val itemsByKey: StateFlow<Map<String, Item>> = _allSprites
        .map { sprites -> sprites.filterIsInstance<Item>().associateBy { it.key } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyMap())

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory.asStateFlow()

    val categoryOptions: StateFlow<List<Category?>> = _allSprites
        .map { sprites -> listOf(null) + sprites.map { it.category }.distinct().sorted() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, listOf(null))

    val showGroupDropdown: StateFlow<Boolean> = _selectedCategory
        .map { it == Category.ITEM }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val _selectedGroup = MutableStateFlow<ItemGroup?>(null)
    val selectedGroup: StateFlow<ItemGroup?> = _selectedGroup.asStateFlow()

    val groupOptions: StateFlow<List<ItemGroup?>> = combine(
        _allSprites,
        _selectedCategory
    ) { sprites, category ->
        if (category == Category.ITEM) {
            listOf(null) + sprites.filterIsInstance<Item>().map { it.group }.distinct().sorted()
        } else {
            listOf(null)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, listOf(null))

    val visibleSprites: StateFlow<List<Sprite>> = combine(
        _allSprites, _selectedCategory, _selectedGroup,
    ) { sprites, category, group ->
        val byCategory =
            if (category == null) sprites else sprites.filter { it.category == category }
        if (category == Category.ITEM && group != null) {
            byCategory.filter { it is Item && it.group == group }
        } else {
            byCategory
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectedSprite = MutableStateFlow<Sprite?>(null)
    val selectedSprite: StateFlow<Sprite?> = _selectedSprite.asStateFlow()

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe.asStateFlow()

    private val _rawMaterials = MutableStateFlow<RawMaterials?>(null)
    val rawMaterials: StateFlow<RawMaterials?> = _rawMaterials.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                repository.load()
                _allSprites.value = repository.getAllSprites()
            } catch (t: Throwable) {
                _error.value = "${t::class.simpleName}: ${t.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onCategorySelected(category: Category?) {
        _selectedCategory.value = category
        _selectedGroup.value = null
    }

    fun onGroupSelected(group: ItemGroup?) {
        _selectedGroup.value = group
    }

    fun onSpriteClick(sprite: Sprite) {
        _selectedSprite.value = sprite
        _recipe.value = null
        _rawMaterials.value = null
        viewModelScope.launch {
            val item = sprite as? Item
            val recipe = item?.let { repository.getRecipe(it.key) }
            val rawMaterials = item?.let { repository.getRawMaterials(it.key) }
            if (_selectedSprite.value == sprite) {
                _recipe.value = recipe
                _rawMaterials.value = rawMaterials
            }
        }
    }

    fun onDialogDismiss() {
        _selectedSprite.value = null
        _recipe.value = null
        _rawMaterials.value = null
    }
}