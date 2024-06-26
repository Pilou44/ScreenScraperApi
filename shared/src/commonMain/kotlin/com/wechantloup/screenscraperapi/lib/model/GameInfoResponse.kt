package com.wechantloup.screenscraperapi.lib.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GameInfoResponse(
    @SerialName("response") val response: Response,
) {

    @Serializable
    internal data class Response(
        @SerialName("jeu") val gameInfo: GameInfo,
    )
}
