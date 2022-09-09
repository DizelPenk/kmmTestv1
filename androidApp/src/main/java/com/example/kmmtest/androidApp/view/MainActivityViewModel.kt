package com.example.kmmtest.androidApp.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kmmtest.androidApp.model.Repository
import com.example.kmmtest.shared.data.Book
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class MainActivityViewModel (repository: Repository): ViewModel() {

    private val bookResult: Flow<PagingData<Book>> = repository.getResult().cachedIn(viewModelScope)

    fun getModelBooks(): Flow<PagingData<Book>> {
        return bookResult
    }
}