package com.example.rickandmortyapp.characters

import androidx.paging.PageKeyedDataSource
import com.example.rickandmortyapp.network.response.GetCharacterByIdResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/*class CharactersDataSource(
    private val coroutineScope: CoroutineScope,
    private val repository: CharactersRepository
) : PagingSource<Int, GetCharacterByIdResponse>() {

    override fun getRefreshKey(state: PagingState<Int, GetCharacterByIdResponse>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null

        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetCharacterByIdResponse> {
        val pageNumber: Int = params.key ?: 1


        val page = repository.getCharactersPage(pageNumber)

        if (page == null) {
            return LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }

        return LoadResult.Page(
            data = page.results,
            prevKey = if (pageNumber == 1) null else pageNumber - 1,
            nextKey = getPageIndexFromNext(page.info.next)
        )


    }

    private fun getPageIndexFromNext(next: String?) : Int? {
        return next?.split("?page=")?.get(1)?.toInt()
    }
}*/
class CharactersDataSource(
    private val coroutineScope: CoroutineScope,
    private val repository: CharactersRepository
) : PageKeyedDataSource<Int, GetCharacterByIdResponse>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, GetCharacterByIdResponse>
    ) {
        coroutineScope.launch {
            val page = repository.getCharactersPage(1)

            if (page == null) {
                callback.onResult(emptyList(), null, null)
            }

            callback.onResult(page!!.results,null, getPageIndexFromNext(page.info.next))
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GetCharacterByIdResponse>
    ) {
        coroutineScope.launch {
            val page = repository.getCharactersPage(params.key)

            if (page == null) {
                callback.onResult(emptyList(),null)
                return@launch
            }

            callback.onResult(page.results, getPageIndexFromNext(page.info.next))
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, GetCharacterByIdResponse>
    ) {
        // nothing to do
    }

    private fun getPageIndexFromNext(next: String?) : Int? {
        return next?.split("?page=")?.get(1)?.toInt()
    }


}
