package com.example.rickandmortyapp.characters

import androidx.lifecycle.viewModelScope
import com.example.rickandmortyapp.domain.mappers.CharacterMapper
import com.example.rickandmortyapp.network.NetworkLayer
import com.example.rickandmortyapp.network.RickAndMortyCache
import com.example.rickandmortyapp.domain.models.Character
import com.example.rickandmortyapp.network.response.GetCharacterByIdResponse
import com.example.rickandmortyapp.network.response.GetCharactersPageResponse
import com.example.rickandmortyapp.network.response.GetEpisodeByIdResponse
import kotlinx.coroutines.launch


class CharactersRepository {

    suspend fun getCharactersPage(pageIndex: Int) : GetCharactersPageResponse? {
        val request = NetworkLayer.apiClient.getCharactersPage(pageIndex)

        if (request.failed || !request.isSuccessful) {
            return null
        }

        return request.body
    }

    suspend fun getCharacterById(characterId: Int): Character? {

        //Check the cache for our character
        val cacheCharacter = RickAndMortyCache.characterMap[characterId]
        if (cacheCharacter != null) {
            return cacheCharacter
        }

        val request = NetworkLayer.apiClient.getCharacterById(characterId)

        if (request.failed || !request.isSuccessful) return null

        val networkEpisode = getEpisodesFromCharacterResponse(request.body)
        val character = CharacterMapper.buildFrom(
            response = request.body,
            episodes = networkEpisode
        )

        // Update cache and return value
        RickAndMortyCache.characterMap[characterId] = character
        return character
    }

    private suspend fun getEpisodesFromCharacterResponse(
        characterResponse: GetCharacterByIdResponse
    ): List<GetEpisodeByIdResponse> {
        val episodeRange = characterResponse.episode.map {
            it.substring(it.lastIndexOf("/") + 1)
        }.toString()

        val request = NetworkLayer.apiClient.getEpisodeRange(episodeRange)

        if (request.failed || !request.isSuccessful) {
            return emptyList()
        }

        return request.body
    }

}