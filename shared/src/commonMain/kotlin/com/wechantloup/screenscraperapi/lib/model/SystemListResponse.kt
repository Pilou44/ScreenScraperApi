package com.wechantloup.screenscraperapi.lib.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SystemListResponse(
    @SerialName("response") val response: Response,
) {

    @Serializable
    internal data class Response(
        @SerialName("systemes") val systems: List<System>,
    )
}
