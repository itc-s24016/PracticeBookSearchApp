package com.example.practicebooksearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicebooksearchapp.ui.theme.PracticeBookSearchAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeBookSearchAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val viewModel: BookViewModel = viewModel()
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Green)
            ){
                Button(onClick = {
                    viewModel.searchBooks()
                    navController.navigate("detail")
                }){
                    Text("リスト画面から詳細へ")
                }
            }
        }
        composable("detail") {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(Color.Blue)
            ){
                Button(onClick = {
                    navController.popBackStack()
                }){
                    Text("詳細画面からリスト画面に戻る")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PracticeBookSearchAppTheme {
        Main()
    }
}