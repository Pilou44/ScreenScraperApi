package com.wechantloup.screenscraperapi.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.screenState.collectAsState()
    val channel = viewModel.screenIntentChannel
    MainScreen(state, channel)
}

@Composable
fun MainScreen(
    state: MainState,
    channel: Channel<ScreenIntent>,
) {
    val scope = rememberCoroutineScope()
    Column {
        TextField(
            value = state.devId.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewDevIdIntent(id)) }
            },
            label = { Text("Dev ID") },
        )
        TextField(
            value = state.devId.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewDevPasswordIntent(id)) }
            },
            label = { Text("Dev password") },
        )
        TextField(
            value = state.devId.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewAppNameIntent(id)) }
            },
            label = { Text("App name") },
        )
        TextField(
            value = state.devId.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewUserIdIntent(id)) }
            },
            label = { Text("User ID") },
        )
        TextField(
            value = state.devId.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewUserPasswordIntent(id)) }
            },
            label = { Text("User password") },
        )
        Row {
            Button(onClick = { scope.launch { channel.send(GetPlatformsIntent) } }) {
                Text("Get platforms")
            }
            Text(state.platformsState)
        }
    }
}
