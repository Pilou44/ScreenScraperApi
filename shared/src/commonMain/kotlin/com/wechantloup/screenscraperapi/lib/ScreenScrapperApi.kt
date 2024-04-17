package com.wechantloup.screenscraperapi.lib

import com.wechantloup.screenscraperapi.lib.ScreenScraper.httpClient
import com.wechantloup.screenscraperapi.lib.model.GameInfo
import com.wechantloup.screenscraperapi.lib.model.GameInfoResponse
import com.wechantloup.screenscraperapi.lib.model.System
import com.wechantloup.screenscraperapi.lib.model.SystemListResponse
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.kodein.log.LoggerFactory
import org.kodein.log.frontend.defaultLogFrontend
import org.kodein.log.newLogger

public interface ScreenScraperApi {
    public suspend fun getSystems(): List<System>
    public suspend fun getGameInfo(
        crcHexa: String,
        systemId: Int,
        romName: String,
        romSize: Long,
        romType: String = "rom",
    ): GameInfo
}

internal class ScreenScraperApiImpl(
    private val devId: String,
    private val devPassword: String,
    private val softName: String,
    private val userId: String?,
    private val userPassword: String?,
): ScreenScraperApi {

    private val loggerFactory = LoggerFactory(defaultLogFrontend)
    private val logger = newLogger(loggerFactory)

    override suspend fun getSystems(): List<System> {
        logger.debug { "Get systems" }
        val ssResponse: SystemListResponse = launchRequest(GET_SYSTEMS_PATH)
        return ssResponse.response.systems
    }

    override suspend fun getGameInfo(
        crcHexa: String,
        systemId: Int,
        romName: String,
        romSize: Long,
        romType: String,
    ): GameInfo {
        logger.debug { "Get game" }
        val ssResponse: GameInfoResponse = launchRequest(GET_GAME_INFO_PATH) {
            url {
                parameters.append("crc", crcHexa)
                parameters.append("systemeid", systemId.toString())
                parameters.append("romnom", romName)
                parameters.append("romtaille", romSize.toString())
                parameters.append("romtype", romType)
            }
        }
        return ssResponse.response.gameInfo
    }

    private suspend inline fun <reified T> launchRequest(url: String, crossinline buildUrl: HttpRequestBuilder.() -> Unit = {}): T {
        return withContext(Dispatchers.IO) {
            val response = httpClient
                .get(BASE_URL) {
                    buildUrl()

                    url {
                        parameters.append("output", "json")
                        parameters.append("devid", devId)
                        parameters.append("devpassword", devPassword)
                        parameters.append("softname", softName)
                        userId?.let { parameters.append("ssid", userId) }
                        userPassword?.let { parameters.append("sspassword", userPassword) }
                    }

                    url {
                        appendPathSegments(url)
                    }
                }
            logger.info { "URL: ${response.request.url}" }
            try {
                response.body<T>()
            } catch (e: Exception) {
                val status = response.status.value
                logger.error { "Response status: $status" }
                val message = response.bodyAsText()
                logger.info { "Response body: \"$message\"" }
                handleError(status, message, e)
                throw e
            }
        }
    }

    private fun handleError(status: Int, message: String, cause: Exception) {
        if (status == 400 && message.contains(MISSING_PARAMS_MSG)) {
            throw MissingUrlParameterException(cause)
        } else if (message.contains(BAD_DEV_IDS_MSG)) {
            throw BadDevIdsException(cause)
        }
    }

    companion object {
        private const val BASE_URL = "https://www.screenscraper.fr/api2/"
        private const val GET_SYSTEMS_PATH = "systemesListe.php"
        private const val GET_GAME_INFO_PATH = "jeuInfos.php"
        private const val BAD_DEV_IDS_MSG = "Erreur de login : Verifier vos identifiants developpeur !"
        private const val MISSING_PARAMS_MSG = "Il manque des champs obligatoires dans l'url"
    }
}
