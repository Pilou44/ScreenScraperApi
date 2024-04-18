package com.wechantloup.screenscraperapi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.rememberNavController
import com.wechantloup.screenscraperapi.app.ui.NavigationHost
import com.wechantloup.screenscraperapi.app.ui.theme.Screen_Scraper_APITheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Screen_Scraper_APITheme {
                Scaffold { padding ->
                    NavigationHost(
                        activity = this,
                        navController = navController,
                        modifier = Modifier.padding(padding),
                    )
                }
                // A surface container using the 'background' color from the theme
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    RegisterScreen(viewModel)
//                }
            }
        }
    }
}
