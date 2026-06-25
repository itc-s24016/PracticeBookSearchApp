package com.example.practicebooksearchapp

data class BookInfo(
    val kind: String,
    val totalItems: Int,
    val items: List<BookItem>?
)

data class BookItem(
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val description: String,
    val imageLinks: ImageLinks?
){
    fun getAuthorsText(): String {
        return authors?.joinToString(", ") ?: ""
    }
}

data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
)