package com.example.rickandmortyapp.characters

import androidx.paging.DataSource
import androidx.paging.InvalidatingPagingSourceFactory
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.rickandmortyapp.network.response.GetCharacterByIdResponse
import com.example.rickandmortyapp.network.response.GetCharactersPageResponse
import kotlinx.coroutines.CoroutineScope


/*class CharactersDataSourceFactory(
    private val coroutineScope: CoroutineScope,
    private val repository: CharactersRepository
) : InvalidatingPagingSourceFactory {


}*/

class CharactersDataSourceFactory(
    private val coroutineScope: CoroutineScope,
    private val repository: CharactersRepository
) : DataSource.Factory<Int, GetCharacterByIdResponse>() {

    override fun create(): DataSource<Int, GetCharacterByIdResponse> {
        return CharactersDataSource(coroutineScope, repository)
    }
}
