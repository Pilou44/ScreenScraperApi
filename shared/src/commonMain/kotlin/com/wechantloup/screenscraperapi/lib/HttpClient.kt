package com.wechantloup.screenscraperapi.lib

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val httpClient = HttpClient {
    // ToDo add cookies management to store user ids https://api.ktor.io/ktor-client/ktor-client-core/io.ktor.client.plugins.cookies/-http-cookies/index.html
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

//fun endpointUrl(baseUrlPattern: String, version: String, provider: ApiDataProvider): String {
//    if (baseUrlPattern.startsWith("/")) {
//        error("Path '$baseUrlPattern' must not start with a '/'")
//    }
//    if (baseUrlPattern.endsWith("/")) {
//        error("Path '$baseUrlPattern' must not end with a '/'")
//    }
//
//    return baseUrlPattern
//        .replace(ApiConstants.PLACEHOLDER_API_VERSION, version)
//        .replace(ApiConstants.PLACEHOLDER_HOST, provider.getHost().lowercase())
//        .replace(ApiConstants.PLACEHOLDER_LANG, provider.getLang().lowercase())
//        .replace(ApiConstants.PLACEHOLDER_COUNTRY_CODE3, provider.getCountryCode().lowercase())
//        .replace(ApiConstants.PLACEHOLDER_CURRENCY_CODE3, provider.getCurrencyCode().lowercase())
//}
//
//object ApiConstants {
//    const val V3_0 = "3.0"
//    const val V3_1 = "3.1"
//    const val V2_0 = "2.0"
//    const val PLACEHOLDER_HOST = "_host_"
//    const val PLACEHOLDER_API_VERSION = "_apiVersion_"
//    const val PLACEHOLDER_LANG = "_lang_"
//    const val PLACEHOLDER_COUNTRY_CODE3 = "_country_"
//    const val PLACEHOLDER_CURRENCY_CODE3 = "_currency_"
//
//    const val BASE_URL_PUBLIC_V3 = "https://$PLACEHOLDER_HOST/api/public/$PLACEHOLDER_API_VERSION"
//    const val BASE_URL_IDENTIFIED_V3 = "https://$PLACEHOLDER_HOST/api/identified/$PLACEHOLDER_API_VERSION"
//    const val BASE_URL_AUTH_V2 = "https://$PLACEHOLDER_HOST/api/v2/$PLACEHOLDER_LANG"
//    const val BASE_URL_PUBLIC_V2 = "https://$PLACEHOLDER_HOST/api/v2/public/$PLACEHOLDER_LANG/$PLACEHOLDER_COUNTRY_CODE3/$PLACEHOLDER_CURRENCY_CODE3"
//    const val BASE_URL_IDENTIFIED_V2 = "https://$PLACEHOLDER_HOST/api/v2/identified/$PLACEHOLDER_LANG"
//}
