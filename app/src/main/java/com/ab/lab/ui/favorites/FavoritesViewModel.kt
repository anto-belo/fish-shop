package com.ab.lab.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.lab.App
import com.ab.lab.db.entity.Favorites
import kotlinx.coroutines.launch

class FavoritesViewModel(private val userId: String) : ViewModel() {

    private val favoritesRepository = App.instance.favoritesRepository

    private val _favorites = MutableLiveData<List<Favorites>>()
    val favorites: LiveData<List<Favorites>> = _favorites

    fun deleteFavorites(favorites: Favorites) {
        viewModelScope.launch {
            favoritesRepository.delete(favorites)
            _favorites.value = favoritesRepository.getAllFavorites(userId)
        }
    }

    fun load() {
        viewModelScope.launch {
            _favorites.value = favoritesRepository.getAllFavorites(userId)
        }
    }

}