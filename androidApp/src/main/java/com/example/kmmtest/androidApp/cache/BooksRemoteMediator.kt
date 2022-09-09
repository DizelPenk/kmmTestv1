package com.example.kmmtest.androidApp.cache

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.kmmtest.shared.BooksSDK
import com.example.kmmtest.shared.data.Book
import com.example.kmmtest.shared.data.RemoteKey
import java.io.IOException

@ExperimentalPagingApi
class BooksRemoteMediator (
    private val sdk: BooksSDK
) : RemoteMediator<Int, Book>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Book>
    ): MediatorResult {

        val page = when (val pageKeyData = getKeyPageData(loadType, state)) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Int
            }
        }

        return try {
            val isEndOfList = sdk.getBooksFromApi(page).isEmpty()
            if (loadType == LoadType.REFRESH) {
                sdk.clearBooks()
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Book>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey
            }
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Book>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.isbn?.let { repoId ->
                sdk.selectRemoteKeyByQuery(repoId)
            }
        }
    }

    private fun getLastRemoteKey(state: PagingState<Int, Book>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { book -> sdk.selectRemoteKeyByQuery(book.isbn) }
    }

    private fun getFirstRemoteKey(state: PagingState<Int, Book>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { cat -> sdk.selectRemoteKeyByQuery(cat.isbn) }
    }
}