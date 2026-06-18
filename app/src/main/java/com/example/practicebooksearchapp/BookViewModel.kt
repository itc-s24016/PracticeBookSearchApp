package com.example.practicebooksearchapp

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import org.json.JSONObject

class BookViewModel: ViewModel() {
    var bookItems by mutableStateOf<List<BookItem>>(emptyList())
        private set

    fun searchBooks(){
        val bookList = mutableListOf<BookItem>()

        val jsonObject = JSONObject(sampleJSON)
        val items = jsonObject.getJSONArray("items")
        for(i in 0 until items.length()){
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
        bookItems = bookList
    }
}