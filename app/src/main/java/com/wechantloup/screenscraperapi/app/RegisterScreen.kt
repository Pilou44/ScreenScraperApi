package com.wechantloup.screenscraperapi.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: MainViewModel,
    onRegistered: () -> Unit,
) {
    val state by viewModel.screenState.collectAsState()
    val channel = viewModel.screenIntentChannel
    RegisterScreen(state, channel, onRegistered)
}

@Composable
fun RegisterScreen(
    state: MainState,
    channel: Channel<ScreenIntent>,
    onRegistered: () -> Unit,
) {
    LaunchedEffect(key1 = state.registered) {
        if (state.registered) onRegistered()
    }

    val scope = rememberCoroutineScope()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        TextField(
            value = state.devId.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewDevIdIntent(id)) }
            },
            label = { Text("Dev ID") },
        )
        TextField(
            value = state.devPassword.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewDevPasswordIntent(id)) }
            },
            label = { Text("Dev password") },
        )
        TextField(
            value = state.softName.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewAppNameIntent(id)) }
            },
            label = { Text("App name") },
        )
        TextField(
            value = state.userId.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewUserIdIntent(id)) }
            },
            label = { Text("User ID") },
        )
        TextField(
            value = state.userPassword.orEmpty(),
            onValueChange = { id: String ->
                scope.launch { channel.send(NewUserPasswordIntent(id)) }
            },
            label = { Text("User password") },
        )
        Button(onClick = { scope.launch { channel.send(RegisterIntent) } }) {
            Text("Register")
        }
        Row {
            Button(onClick = { scope.launch { channel.send(GetPlatformsIntent) } }) {
                Text("Get platforms")
            }
            Text(state.platformsState)
        }
        Row {
            Button(onClick = { scope.launch { channel.send(GetGameIntent) } }) {
                Text("Get game")
            }
            Text(state.gameState)
        }
        Row {
            Button(onClick = { scope.launch { channel.send(SearchGameIntent) } }) {
                Text("Search game")
            }
            Text(state.searchGameState)
        }
    }
}
