package com.wechantloup.screenscraperapi.lib.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class GameInfoResponse(
    @SerialName("response") val response: Response,
)

@Serializable
internal data class Response(
    @SerialName("jeu") val gameInfo: GameInfo,
)

@Serializable
public data class GameInfo(
    @SerialName("id") val id: String?,
    @SerialName("noms") val names: List<RegionString>?,
    @SerialName("synopsis") val synopsis: List<LanguageString>?,
    @SerialName("note") val rating: TextString?,
    @SerialName("dates") val dates: List<RegionString>?,
    @SerialName("developpeur") val developer: IdString?,
    @SerialName("editeur") val publisher: IdString?,
    @SerialName("genres") val genres: List<Genre>?,
    @SerialName("joueurs") val players: TextString?,
    @SerialName("notgame") val unknownGame: Boolean,
    @SerialName("medias") val medias: List<Media>,
    @SerialName("roms") val roms: List<Rom>
)

@Serializable
public data class Rom(
    @SerialName("romfilename") val fileName: String?,
    @SerialName("romcrc") val crc: String?,
    @SerialName("regions") val regions: Region?,
)

@Serializable
public data class Region(
    @SerialName("regions_shortname") val shortNames: List<String>,
)

@Serializable
public data class TextString(
    @SerialName("text") val text: String,
)

@Serializable
public data class IdString(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String,
)

@Serializable
public data class Genre(
    @SerialName("id") val id: String,
    @SerialName("noms") val names: List<LanguageString>,
)

@Serializable
public data class RegionString(
    @SerialName("region") val region: String,
    @SerialName("text") val text: String,
)

@Serializable
public data class LanguageString(
    @SerialName("langue") val language: String,
    @SerialName("text") val text: String,
)

@Serializable
public data class Media(
    @SerialName("type") val type: String,
    @SerialName("url") val url: String,
    @SerialName("region") val region: String?,
    @SerialName("format") val format: String,
)
