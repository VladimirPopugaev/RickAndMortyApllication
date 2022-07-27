package com.example.rickandmortyapp.screens.episodeListScreen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickandmortyapp.domain.mappers.EpisodeMapper
import com.example.rickandmortyapp.domain.models.Episode
import com.example.rickandmortyapp.episodes.EpisodesRepository
import com.example.rickandmortyapp.network.NetworkLayer
import com.example.rickandmortyapp.screens.episodeListScreen.headers.EpisodeUiModel
import kotlinx.coroutines.CoroutineScope

class EpisodePagingSource(
    private val repository: EpisodesRepository
) : PagingSource<Int, EpisodeUiModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeUiModel> {
        val pageNumber = params.key ?: 1
        val prevKey = if (pageNumber == 1) null else pageNumber - 1

        val pageRequest = NetworkLayer.apiClient.getEpisodesPage(pageNumber)
        pageRequest.exception?.let {
            return LoadResult.Error(it)
        }


        return LoadResult.Page(
            data = pageRequest.body.results.map { response ->
                EpisodeUiModel.Item(EpisodeMapper.buildFrom(response))
            },
            prevKey = null,
            nextKey = getPageIndexFromNext(pageRequest.body.info.next)
        )
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodeUiModel>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getPageIndexFromNext(next: String?): Int? {
        return next?.split("?page=")?.get(1)?.toInt()
    }
}