package com.wechantloup.screenscraperapi.lib.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SearchGameResponse(
    @SerialName("response") val response: Response,
) {

    @Serializable
    internal data class Response(
        @SerialName("jeux") val games: List<GameInfo>,
    )
}
