package com.example.practicebooksearchapp

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import org.json.JSONObject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class BookViewModel: ViewModel() {
    var bookItems by mutableStateOf<List<BookItem>>(emptyList())
        private set

    var query by mutableStateOf("芥川龍之介")
        private set
    fun updateQuery(newQuery: String){
        query = newQuery
    }

    var selectedBook by mutableStateOf<BookItem?>(null)
        private set
    fun selectBook(book: BookItem){
        selectedBook = book
    }

    var isLoading by mutableStateOf(false)
        private set
    val isSearchEnabled get() = query.isNotEmpty() && !isLoading

    fun searchBooks() {
        viewModelScope.launch {
            isLoading = true
            val bookList = withContext(Dispatchers.IO) {
                val bookList = mutableListOf<BookItem>()
                val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
                val apiUrl =
                    "https://www.googleapis.com/books/v1/volumes?q=inauthor:${encodedQuery}&maxResults=20" + "&key=AIzaSyCG1xGiX5E-7UFdOWwb3m5JaFJH1u1CzQg"
                val client = OkHttpClient()
                val request = Request.Builder().url(apiUrl).get().build()
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""
                val jsonObject = JSONObject(responseBody)
                val items = jsonObject.getJSONArray("items")
                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val volumeInfo = item.getJSONObject("volumeInfo")
                    val title = volumeInfo.getString("title")
                    val description = volumeInfo.optString("description", "")
                    Log.d("BookSearch", "title=$title, discription=$description")
                    val vi = VolumeInfo(
                        title = title,
                        authors = null,
                        description = description,
                        imageLinks = null
                    )
                    bookList.add(BookItem(vi))
                }
                bookList
            }
            bookItems = bookList
            isLoading = false
        }
    }
}