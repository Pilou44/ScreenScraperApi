package com.wechantloup.screenscraperapi.app.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wechantloup.screenscraperapi.app.ui.register.NewUserPasswordIntent
import com.wechantloup.screenscraperapi.lib.model.System
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val state by viewModel.screenState.collectAsState()
    val channel = viewModel.screenIntentChannel
    MainScreen(state, channel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    state: MainState,
    channel: Channel<ScreenIntent>,
) {
    if (state.systems.isEmpty() || state.selectedSystemIndex < 0) return

    val scope = rememberCoroutineScope()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Search for a game"
        )

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
        ) {
            val selectedSystem = state.systems[state.selectedSystemIndex].getName()
            OutlinedTextField(
                label = { Text("System") },
                value = selectedSystem,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                state.systems.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(text = selectionOption.getName()) },
                        onClick = {
                            expanded = false
                            scope.launch { channel.send(SelectSystemIntent(selectionOption)) }
                        },
                    )
                }
            }
        }
    }
}

private fun System?.getName(): String = if (this == null) {
    "All systems"
} else {
    names.euName ?: names.usName ?: names.jpName ?: names.retropieName ?: id.toString()
}
