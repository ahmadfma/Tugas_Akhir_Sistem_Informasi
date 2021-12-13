package com.example.indonews_app.Model

class DataNews : ArrayList<DataNewsItem>()

data class DataNewsItem(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

data class Article(
    val source: Source,
    val author: Any,
    val title: String,
    val description: Any,
    val url: String,
    val urlToImage: Any,
    val publishedAt: String,
    val content: Any
)

data class Source(
    val id: String?,
    val name: String?
)