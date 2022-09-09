package com.example.kmmtest.shared.cache

import com.example.kmmtest.shared.data.Book
import com.example.kmmtest.shared.data.RemoteKey

class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = BooksDatabase(databaseDriverFactory.createDriver())
    private val databaseQuery = database.booksDatabaseQueries

    fun clearDatabase() {
        databaseQuery.transaction {
            databaseQuery.removeAllBooks()
            databaseQuery.removeAllRemoteKeys()
        }
    }

    fun getAllBooks(): List<Book> {
        return databaseQuery.selectAllBooks(::mapBookSelecting).executeAsList()
    }

    private fun mapBookSelecting(
        id: String,
        isbn: String,
        title: String,
        description: String,
        author: String,
        publicationDate: String
    ): Book {
        return Book(
            id = id,
            isbn = isbn,
            title = title,
            description = description,
            author = author,
            publicationDate = publicationDate
        )
    }

    fun selectRemoteKeyByQuery(id: String): RemoteKey {
        return databaseQuery.selectRemoteKeyByQuery(id, ::mapRemoteKeySelecting).executeAsOne()
    }

    private fun mapRemoteKeySelecting(
        id: String,
        prevKey: Int?,
        nextKey: Int?
    ): RemoteKey {
        return RemoteKey(
            id = id,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    fun createBooks(books: List<Book>) {
        databaseQuery.transaction {
            books.forEach { book ->
                insertBook(book)
            }
        }
    }

    private fun insertBook(book: Book) {
        databaseQuery.insertBook(
            id = book.id,
            isbn = book.isbn,
            title = book.title,
            description = book.description,
            author = book.author,
            publicationDate = book.publicationDate
        )
    }

    fun createRemoteKeys(remoteKeys: List<RemoteKey>) {
        databaseQuery.transaction {
            remoteKeys.forEach { remoteKey ->
                insertRemoteKey(remoteKey)
            }
        }
    }

    private fun insertRemoteKey(remoteKey: RemoteKey) {
        databaseQuery.insertRemoteKey(
            id = remoteKey.id,
            prevKey = remoteKey.prevKey,
            nextKey = remoteKey.nextKey
        )
    }
}