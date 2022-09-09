package com.example.kmmtest.shared.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Book(
    @SerialName("id")
    val id: String,
    @SerialName("isbn")
    val isbn: String,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("author")
    val author: String,
    @SerialName("publicationDate")
    val publicationDate: String
)
