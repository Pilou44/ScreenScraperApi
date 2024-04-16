package com.wechantloup.screenscraperapi.lib

import com.wechantloup.screenscraperapi.lib.model.System
import com.wechantloup.screenscraperapi.lib.model.SystemListResponse
import io.ktor.client.call.body
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
        return withContext(Dispatchers.IO) {
            val response = httpClient
                .get(BASE_URL) {
                    url {
                        parameters.append("output", "json")
                        parameters.append("devid", devId)
                        parameters.append("devpassword", devPassword)
                        parameters.append("softname", softName)
                        userId?.let { parameters.append("ssid", userId) }
                        userPassword?.let { parameters.append("sspassword", userPassword) }
                    }

                    url {
                        appendPathSegments(GET_SYSTEMS_PATH)
                    }
                }
            logger.info { "URL: ${response.request.url}" }
            try {
                val ssRsponse: SystemListResponse = response.body()
                ssRsponse.response.systems
            } catch (e: Exception) {
                val body = response.bodyAsText()
                val status = response.status.value
                logger.error { "Response status: $status" }
                if (body == BAD_DEV_IDS_MSG) {
                    throw BadDevIdsException(e)
                }
                logger.info { "Response body: \"$body\"" }
                throw e
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://www.screenscraper.fr/api2/"
        private const val GET_SYSTEMS_PATH = "systemesListe.php"
        private const val BAD_DEV_IDS_MSG = "\uFEFFErreur de login : Vérifier vos identifiants développeur !"
    }
}
