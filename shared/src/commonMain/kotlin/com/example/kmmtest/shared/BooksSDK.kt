package com.example.kmmtest.shared

import com.example.kmmtest.shared.cache.Database
import com.example.kmmtest.shared.cache.DatabaseDriverFactory
import com.example.kmmtest.shared.data.Book
import com.example.kmmtest.shared.data.RemoteKey
import com.example.kmmtest.shared.network.Api

class BooksSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = Api()

    @Throws(Exception::class) suspend fun getBooksFromApi(page: Int): List<Book> {

        val books = api.getAllBooks(page)
        val isEndOfList = books.isEmpty()

        val prevKey = if (page == 1) null else page - 1
        val nextKey = if (isEndOfList) null else page + 1
        val keys = books.map {
            RemoteKey(it.isbn, prevKey = prevKey, nextKey = nextKey)
        }
        database.createRemoteKeys(keys)
        database.createBooks(books)

        return books
    }

    @Throws(Exception::class) fun getBooksFromDatabase(): List<Book> {
        return database.getAllBooks()
    }

    @Throws(Exception::class) fun clearBooks() {
        database.clearDatabase()
    }

    @Throws(Exception::class) fun selectRemoteKeyByQuery(id: String): RemoteKey {
        return database.selectRemoteKeyByQuery(id)
    }
}