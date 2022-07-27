package com.example.rickandmortyapp.screens.episodeListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.rickandmortyapp.Constants
import com.example.rickandmortyapp.episodes.EpisodesRepository
import com.example.rickandmortyapp.screens.episodeListScreen.headers.EpisodeUiModel
import kotlinx.coroutines.flow.map

class EpisodeListViewModel : ViewModel() {

    private val repository = EpisodesRepository()

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(
            pageSize = Constants.PAGE_SIZE,
            prefetchDistance = Constants.PREFETCH_DISTANCE,
            enablePlaceholders = false
        )
    ) {
        EpisodePagingSource(repository)
    }.flow.cachedIn(viewModelScope).map {
        it.insertSeparators { model1: EpisodeUiModel?, model2: EpisodeUiModel? ->
            if (model1 == null) return@insertSeparators EpisodeUiModel.Header("Season 1")

            if (model2 == null) return@insertSeparators null

            if (model1 is EpisodeUiModel.Header || model2 is EpisodeUiModel.Header)
                return@insertSeparators null

            val episode1 = (model1 as EpisodeUiModel.Item).episode
            val episode2 = (model2 as EpisodeUiModel.Item).episode

            return@insertSeparators if (episode1.seasonNumber != episode2.seasonNumber) {
                EpisodeUiModel.Header("Season ${episode2.seasonNumber}")
            } else null

        }
    }

}