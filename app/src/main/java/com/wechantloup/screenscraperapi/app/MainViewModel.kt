package com.wechantloup.screenscraperapi.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    application: Application,
): AndroidViewModel(application) {

    private val _screenState: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val screenState: StateFlow<MainState> = _screenState

    val screenIntentChannel = Channel<ScreenIntent>(Channel.UNLIMITED)
}

data class MainState(
    val devId: String? = null,
    val devPassword: String? = null,
    val softName: String? = null,
    val userId: String? = null,
    val userPassword: String? = null,
    val platformsState: String = "Not launched"
)

sealed interface ScreenIntent
data class NewDevIdIntent(val id: String): ScreenIntent
data class NewDevPasswordIntent(val id: String): ScreenIntent
data class NewAppNameIntent(val id: String): ScreenIntent
data class NewUserIdIntent(val id: String): ScreenIntent
data class NewUserPasswordIntent(val id: String): ScreenIntent
data object GetPlatformsIntent: ScreenIntent
