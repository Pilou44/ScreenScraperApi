package com.wechantloup.screenscraperapi.lib

object ScreenScraper {

    private var devId: String? = null
    private var devPassword: String? = null
    private var softName: String? = null
    private var userId: String? = null
    private var userPassword: String? = null

    private var registered: Boolean = false

    fun register(
        devId: String,
        devPassword: String,
        softName: String,
    ) {
        this.devId = devId
        this.devPassword = devPassword
        this.softName = softName
        registered = true
    }

    fun logIn(
        userId: String,
        userPassword: String,
    ) {
        this.userId = userId
        this.userPassword = userPassword
    }

    val api: ScreenScraperApi by lazy {
        if (!registered) {
            throw NotRegisteredException()
        }
        ScreenScraperApiImpl(
            devId = requireNotNull(devId),
            devPassword = requireNotNull(devPassword),
            softName =requireNotNull(softName),
            userId = requireNotNull(userId),
            userPassword = requireNotNull(userPassword),
        )
    }
}

class NotRegisteredException: Exception("Dev account should be registered")