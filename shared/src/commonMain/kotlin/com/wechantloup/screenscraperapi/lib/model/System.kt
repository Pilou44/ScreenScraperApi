package com.wechantloup.screenscraperapi.lib.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class System(
    @SerialName("id") val id: Int,
    @SerialName("noms") val names: Names,
    @SerialName("extensions") val extensions: String?,
)

@Serializable
public data class Names(
    @SerialName("nom_eu") val euName: String?,
    @SerialName("nom_us") val usName: String?,
    @SerialName("nom_jp") val jpName: String?,
    @SerialName("nom_retropie") val retropieName: String?,
)
