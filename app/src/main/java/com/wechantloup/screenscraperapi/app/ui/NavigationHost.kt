package com.wechantloup.screenscraperapi.app.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.wechantloup.screenscraperapi.app.ui.main.MainScreen
import com.wechantloup.screenscraperapi.app.ui.main.MainViewModel
import com.wechantloup.screenscraperapi.app.ui.register.RegisterViewModel
import com.wechantloup.screenscraperapi.app.ui.register.RegisterScreen

// Screens
private const val REGISTER_SCREEN = "register_screen"
private const val MAIN_SCREEN = "main_screen"
//private const val EDIT_PLATFORM_SCREEN = "edit_platform_screen"
//private const val GAME_SCREEN = "game_screen"
//private const val EDIT_GAME_SCREEN = "edit_game_screen"
//private const val SETTINGS_SCREEN = "settings_screen"

@Composable
fun NavigationHost(
    activity: ComponentActivity,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        if (navController.popBackStack()) return@BackHandler
        activity.finish()
    }

    NavHost(navController = navController, startDestination = REGISTER_SCREEN, modifier) {

        composable(REGISTER_SCREEN) {
            val viewModel = getRegisterViewModel(activity = activity)
            viewModel.resetState()
            RegisterScreen(
                viewModel = viewModel,
                onRegistered = { navController.navigate(MAIN_SCREEN) { popUpTo(navController.graph.id) { inclusive = false} } },
            )
        }
        composable(MAIN_SCREEN) {
            val viewModel = getMainViewModel(activity = activity)
            MainScreen(viewModel)
        }

//        composable(EDIT_PLATFORM_SCREEN) {
//            EditPlatformScreen(
//                viewModel = platformViewModel,
//                onBackPressed = {
//                    mainViewModel.refresh()
//                    navController.popBackStack(route = MAIN_SCREEN, inclusive = false)
//                },
//            )
//        }
//
//        composable(GAME_SCREEN) {
//            GameScreen(
//                viewModel = gameViewModel,
//                onBackPressed = {
//                    mainViewModel.refresh()
//                    navController.popBackStack(route = MAIN_SCREEN, inclusive = false)
//                },
//                onEditClicked = {
//                    navController.navigate(EDIT_GAME_SCREEN)
//                },
//            )
//        }
//
//        composable(EDIT_GAME_SCREEN) {
//            EditGameScreen(
//                viewModel = gameViewModel,
//                onBackPressed = { navController.popBackStack(route = GAME_SCREEN, inclusive = false) },
//            )
//        }
//
//        composable(SETTINGS_SCREEN) {
//            settingsViewModel.reload()
//            SettingsScreen(
//                viewModel = settingsViewModel,
//                onBackPressed = { navController.popBackStack(route = MAIN_SCREEN, inclusive = false) },
//            )
//        }
    }
}

@Composable
private fun getRegisterViewModel(
    activity: ComponentActivity,
) = viewModel<RegisterViewModel>(viewModelStoreOwner = activity)

@Composable
private fun getMainViewModel(
    activity: ComponentActivity,
) = viewModel<MainViewModel>(viewModelStoreOwner = activity)
