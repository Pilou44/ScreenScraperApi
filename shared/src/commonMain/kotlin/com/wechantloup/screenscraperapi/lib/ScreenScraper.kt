package com.wechantloup.screenscraperapi.lib

import com.wechantloup.screenscraperapi.lib.model.GameInfo
import com.wechantloup.screenscraperapi.lib.model.System
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.cookie
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

public object ScreenScraper: ScreenScraperApi {

    private var devId: String? = null
    private var devPassword: String? = null
    private var softName: String? = null
    private var userId: String? = null
    private var userPassword: String? = null

    private var registered: Boolean = false

    private val cookyStore = CleanableCookiesStorage()
    private var api: ScreenScraperApi? = null

    @OptIn(ExperimentalSerializationApi::class)
    internal val httpClient = HttpClient {
        install(HttpCookies) {
            storage = cookyStore
        }
        defaultRequest {
            cookie("myCookie", "true", path = "/")
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay()
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    useAlternativeNames = false
                    isLenient = true
                    explicitNulls = false
                }
            )
        }
    }

    public fun register(
        devId: String,
        devPassword: String,
        softName: String,
    ) {
        this.devId = devId
        this.devPassword = devPassword
        this.softName = softName
        registered = true
        clear()
    }

    public fun logIn(
        userId: String?,
        userPassword: String?,
    ) {
        this.userId = userId?.takeIf { it.isNotBlank() }
        this.userPassword = userPassword?.takeIf { it.isNotBlank() }
        if (registered) clear()
    }

    private fun clear() {
        cookyStore.clear()
        api = ScreenScraperApiImpl(
            devId = requireNotNull(devId),
            devPassword = requireNotNull(devPassword),
            softName = requireNotNull(softName),
            userId = userId,
            userPassword = userPassword,
        )
    }

    override suspend fun getSystems(): List<System> {
        val api = api ?: throw NotRegisteredException()
        return api.getSystems()
    }

    override suspend fun getGameInfo(
        crcHexa: String,
        systemId: Int,
        romName: String,
        romSize: Long,
        romType: String,
    ): GameInfo {
        val api = api ?: throw NotRegisteredException()
        return api.getGameInfo(crcHexa, systemId, romName, romSize, romType)
    }

    override suspend fun searchGame(name: String, systemId: Int?): List<GameInfo> {
        val api = api ?: throw NotRegisteredException()
        return api.searchGame(name, systemId)
    }
}
