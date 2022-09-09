package com.example.kmmtest.shared.network

import com.example.kmmtest.shared.data.Book
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json


class Api {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun getAllBooks(page: Int): List<Book> {
        return httpClient.get {
            url {
                protocol = URLProtocol.HTTPS
                host = BASE_URL
                encodedPath = "/books?page=$page"
            }
        }.body()
    }

    companion object {
        private const val BASE_URL = "demo.api-platform.com"
    }
}