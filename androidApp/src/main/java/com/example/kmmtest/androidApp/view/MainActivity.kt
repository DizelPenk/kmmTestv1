package com.example.kmmtest.androidApp.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kmmtest.androidApp.R
import com.example.kmmtest.androidApp.model.Repository
import com.example.kmmtest.androidApp.model.ViewModelFactory
import com.example.kmmtest.androidApp.view.adapter.Adapter
import com.example.kmmtest.androidApp.view.adapter.BooksAdapter
import com.example.kmmtest.shared.BooksSDK
import com.example.kmmtest.shared.cache.DatabaseDriverFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private val booksAdapter = BooksAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        val sdk = BooksSDK(DatabaseDriverFactory(this))
        val repository = Repository(sdk)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(MainActivityViewModel::class.java)

        initAdapter()
        loadData()
        refreshApp()
    }

    private fun loadData() {
        lifecycleScope.launch {
            viewModel.getModelBooks().collectLatest { pageData->
                booksAdapter.submitData(pageData)
            }
        }

        lifecycleScope.launch {
            booksAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { recyclerView.scrollToPosition(0) }
        }
    }

    private fun initAdapter() {
        recyclerView.adapter = booksAdapter.withLoadStateHeaderAndFooter(
            header = Adapter{booksAdapter.retry()},
            footer = Adapter{booksAdapter.retry()}
        )
        booksAdapter.addLoadStateListener { loadState->
            progressbar.isVisible = loadState.source.refresh is LoadState.Loading
            recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
        }
    }

    private fun refreshApp() {

        swipeToRefresh.setOnRefreshListener {
            booksAdapter.refresh()
            swipeToRefresh.isRefreshing = false
            Toast.makeText(this, getString(R.string.data_updated), Toast.LENGTH_SHORT).show()
        }
    }
}