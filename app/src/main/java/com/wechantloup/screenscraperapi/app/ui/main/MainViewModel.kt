package com.wechantloup.screenscraperapi.app.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wechantloup.screenscraperapi.lib.ScreenScraper
import com.wechantloup.screenscraperapi.lib.model.GameInfo
import com.wechantloup.screenscraperapi.lib.model.System
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

    init {
        viewModelScope.launch {
            screenIntentChannel.consumeEach { handleIntent(it) }
        }

        _screenState.value = screenState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val systems = listOf(null) + ScreenScraper.getSystems()
                _screenState.value = screenState.value.copy(
                    isLoading = false,
                    systems = systems,
                    selectedSystemIndex = 0,
                )
            } catch (e: Exception) {
                // ToDo handle exception
                _screenState.value = screenState.value.copy(isLoading = false)
            }
        }
    }

    private fun System.getName(): String {
        return names.euName ?: names.usName ?: names.jpName ?: names.retropieName ?: id.toString()
    }

    private fun handleIntent(intent: ScreenIntent) {
        when (intent) {
            is SelectSystemIntent -> setSystem(intent.system)
            is SearchIntent -> search()
            is SetNameIntent -> setName(intent.name)
        }
    }

    private fun setSystem(system: System?) {
        val systems = screenState.value.systems
        _screenState.value = screenState.value.copy(selectedSystemIndex = systems.indexOf(system))
    }

    private fun setName(name: String) {
        _screenState.value = screenState.value.copy(name = name)
    }

    private fun search() {
        val name = screenState.value.name

        if (name.isBlank()) return

        val systems = screenState.value.systems
        val index = screenState.value.selectedSystemIndex
        val system = systems[index]

        viewModelScope.launchLoading {
            val result = ScreenScraper.searchGame(name, system?.id)
            _screenState.value = _screenState.value.copy(result = "${result.size} games found")
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
}

data class MainState(
    val isLoading: Boolean = false,
    val systems: List<System?> = emptyList(),
    val selectedSystemIndex: Int = -1,
    val name: String = "",
    val result: String = "",
    val games: List<GameInfo> = emptyList(),
)

sealed interface ScreenIntent
data class SelectSystemIntent(val system: System?): ScreenIntent
data class SetNameIntent(val name: String): ScreenIntent
data object SearchIntent: ScreenIntent
