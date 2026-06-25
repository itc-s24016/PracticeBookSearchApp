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
import com.google.gson.Gson
import okio.IOException

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
    var message by mutableStateOf("")
        private set
    fun clearMessage() {
        message = ""
    }
    val isSearchEnabled get() = query.isNotEmpty() && !isLoading

    fun searchBooks() {
        viewModelScope.launch {
            isLoading = true
            try {
                val bookList = withContext(Dispatchers.IO) {
                    val encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
                    val apiUrl =
                        "https://www.googleapis.com/books/v1/volumes?q=inauthor:${encodedQuery}&maxResults=20" + "&key=AIzaSyCG1xGiX5E-7UFdOWwb3m5JaFJH1u1CzQg"
                    val client = OkHttpClient()
                    val request = Request.Builder().url(apiUrl).get().build()
                    val responseBody = client.newCall(request).execute().use { response ->
                        if (!response.isSuccessful){
                            throw Exception("HTTP ステータス：${response.code}")
                        }
                        response.body?.string() ?: ""
                    }
                    val gson = Gson()
                    val res = gson.fromJson(responseBody, BookInfo::class.java)
                    val bookList = res.items ?: emptyList()
                    return@withContext bookList
                }
                bookItems = bookList

                if (bookItems.isEmpty()) {
                    message = "該当データなし"
                }
            } catch (e: IOException){
                message = "通信エラーが発生しました。時間をおいて再試行してください。\n${e.message}"
            } catch (e: Exception){
                message = "書籍情報の取得に失敗しました。\n${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}