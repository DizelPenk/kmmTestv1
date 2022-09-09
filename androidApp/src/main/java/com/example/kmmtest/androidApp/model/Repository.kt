package com.example.kmmtest.androidApp.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.kmmtest.androidApp.cache.BooksPagingSource
import com.example.kmmtest.androidApp.cache.BooksRemoteMediator
import com.example.kmmtest.shared.BooksSDK
import com.example.kmmtest.shared.data.Book
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class Repository (private val sdk: BooksSDK) {
    fun getResult(): Flow<PagingData<Book>>{
        val pagingSourceFactory = { BooksPagingSource(sdk.getBooksFromDatabase()) }
        return Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false),
            remoteMediator = BooksRemoteMediator(sdk),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}