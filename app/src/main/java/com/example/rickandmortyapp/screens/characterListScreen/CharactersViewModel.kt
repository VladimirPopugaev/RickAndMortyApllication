package com.example.rickandmortyapp.screens.characterListScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.rickandmortyapp.Constants
import com.example.rickandmortyapp.characters.CharactersDataSourceFactory
import com.example.rickandmortyapp.characters.CharactersRepository
import com.example.rickandmortyapp.network.response.GetCharacterByIdResponse

class CharactersViewModel : ViewModel() {

    private val repository = CharactersRepository()

    /*private val pageListConfig: PagingConfig = PagingConfig(
        pageSize = Constants.PAGE_SIZE,
        prefetchDistance = Constants.PREFETCH_DISTANCE
    )*/

    private val pageListConfig: PagedList.Config = PagedList.Config.Builder()
        .setPageSize(Constants.PAGE_SIZE)
        .setPrefetchDistance(Constants.PREFETCH_DISTANCE)
        .build()

    private val dataSourceFactory = CharactersDataSourceFactory(viewModelScope, repository)

    val charactersPagedListLiveData: LiveData<PagedList<GetCharacterByIdResponse>> =
        LivePagedListBuilder(dataSourceFactory, pageListConfig).build()

}