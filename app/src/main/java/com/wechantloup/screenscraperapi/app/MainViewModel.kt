package com.wechantloup.screenscraperapi.app

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wechantloup.screenscraperapi.lib.BadDevIdsException
import com.wechantloup.screenscraperapi.lib.NotRegisteredException
import com.wechantloup.screenscraperapi.lib.ScreenScraper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application,
): AndroidViewModel(application) {

    private val _screenState: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    val screenState: StateFlow<MainState> = _screenState

    val screenIntentChannel = Channel<ScreenIntent>(Channel.UNLIMITED)

    private val stringStore = StringStore(application)

    init {
        viewModelScope.launch {
            screenIntentChannel.consumeEach { handleIntent(it) }
        }

        viewModelScope.launch {
            val appName = getAppName()
            val devId = getDevId()
            val devPassword = getDevPassword()
            val userId = getUserId()
            val userPassword = getUserPassword()

            _screenState.value = screenState.value.copy(
                softName = appName,
                devId = devId,
                devPassword = devPassword,
                userId = userId,
                userPassword = userPassword,
            )
        }
    }

    private fun handleIntent(intent: ScreenIntent) {
        when (intent) {
            is GetPlatformsIntent -> viewModelScope.launchLoading { getPlatforms() }
            is NewAppNameIntent -> viewModelScope.launch { saveAppName(intent.name) }
            is NewDevIdIntent -> viewModelScope.launch { saveDevId(intent.id) }
            is NewDevPasswordIntent -> viewModelScope.launch { saveDevPassword(intent.pwd) }
            is NewUserIdIntent -> viewModelScope.launch { saveUserId(intent.id) }
            is NewUserPasswordIntent -> viewModelScope.launch { saveUserPassword(intent.pwd) }
        }
    }

    private fun CoroutineScope.launchLoading(
        block: suspend CoroutineScope.() -> Unit
    ) {
        if (_screenState.value.isLoading) return
        _screenState.value = _screenState.value.copy(isLoading = true)
        launch(Dispatchers.Default) {
            block()
            _screenState.value = _screenState.value.copy(isLoading = false)
        }
    }

    private suspend fun getPlatforms() {
        try {
            val platforms = ScreenScraper.getSystems()
            _screenState.value = screenState.value.copy(platformsState = "${platforms.size} platforms found")
        } catch (e: NotRegisteredException) {
            _screenState.value = screenState.value.copy(platformsState = "Not registered")
        } catch (e: BadDevIdsException) {
            _screenState.value = screenState.value.copy(platformsState = "Bad dev ids")
        } catch (e: Exception) {
            Log.e("MainViewModel", "Unkown error", e)
            _screenState.value = screenState.value.copy(platformsState = "Unknown error")
        }
    }

    private suspend fun getAppName(): String? {
        return getString(APP_NAME_ID)
    }

    private suspend fun getDevId(): String? {
        return getString(DEV_ID_ID)
    }

    private suspend fun getDevPassword(): String? {
        return getString(DEV_PASSWORD_ID)
    }

    private suspend fun getUserId(): String? {
        return getString(USER_ID_ID)
    }

    private suspend fun getUserPassword(): String? {
        return getString(USER_PASSWORD_ID)
    }

    private suspend fun saveAppName(name: String) {
        _screenState.value = screenState.value.copy(softName = name)
        saveString(APP_NAME_ID, name)
        register()
    }

    private suspend fun saveDevId(id: String) {
        _screenState.value = screenState.value.copy(devId = id)
        saveString(DEV_ID_ID, id)
        register()
    }

    private suspend fun saveDevPassword(pwd: String) {
        _screenState.value = screenState.value.copy(devPassword = pwd)
        saveString(DEV_PASSWORD_ID, pwd)
        register()
    }

    private suspend fun saveUserId(id: String) {
        _screenState.value = screenState.value.copy(userId = id)
        saveString(USER_ID_ID, id)
        login()
    }

    private suspend fun saveUserPassword(pwd: String) {
        _screenState.value = screenState.value.copy(userPassword = pwd)
        saveString(USER_PASSWORD_ID, pwd)
        login()
    }

    private suspend fun register() {
        val appName = getAppName() ?: return
        val devId = getDevId() ?: return
        val devPassword = getDevPassword() ?: return
        ScreenScraper.register(devId, devPassword, appName)
    }

    private suspend fun login() {
        val userId = getUserId()
        val userPassword = getUserPassword()
        ScreenScraper.logIn(userId, userPassword)
    }

    private suspend fun getString(id: String): String? {
        return stringStore.get(id)?.takeIf { it.isNotBlank() }
    }

    private suspend fun saveString(id: String, value: String) {
        val toSave = value.takeIf { it.isNotBlank() }
        if (toSave == null) {
            stringStore.delete(id)
            return
        }

        stringStore.save(id, toSave)
    }

    companion object {
        private const val APP_NAME_ID = "app_name"
        private const val DEV_ID_ID = "dev_id"
        private const val DEV_PASSWORD_ID = "dev_password"
        private const val USER_ID_ID = "user_id"
        private const val USER_PASSWORD_ID = "user_password"
    }
}

data class MainState(
    val isLoading: Boolean = false,
    val devId: String? = null,
    val devPassword: String? = null,
    val softName: String? = null,
    val userId: String? = null,
    val userPassword: String? = null,
    val platformsState: String = "Not launched"
)

sealed interface ScreenIntent
data class NewDevIdIntent(val id: String): ScreenIntent
data class NewDevPasswordIntent(val pwd: String): ScreenIntent
data class NewAppNameIntent(val name: String): ScreenIntent
data class NewUserIdIntent(val id: String): ScreenIntent
data class NewUserPasswordIntent(val pwd: String): ScreenIntent
data object GetPlatformsIntent: ScreenIntent
