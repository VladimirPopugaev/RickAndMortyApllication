package com.example.rickandmortyapp.screens.characterDetailsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.SharedRepository
import kotlinx.coroutines.launch
import com.example.rickandmortyapp.domain.models.Character
import com.example.rickandmortyapp.network.RickAndMortyCache

class SharedViewModel : ViewModel() {

    private val repository = SharedRepository()

    private val _characterByIdLiveData = MutableLiveData<Character?>()
    val characterByIdLiveData: LiveData<Character?> = _characterByIdLiveData

    fun refreshCharacter(characterId: Int) {
        val cacheCharacter = RickAndMortyCache.characterMap[characterId]

        if (cacheCharacter != null) {
            _characterByIdLiveData.postValue(cacheCharacter)
            return
        }

        viewModelScope.launch {
            val response = repository.getCharacterById(characterId)

            _characterByIdLiveData.postValue(response)

            response?.let {
                RickAndMortyCache.characterMap[characterId] = it
            }
        }
    }
}