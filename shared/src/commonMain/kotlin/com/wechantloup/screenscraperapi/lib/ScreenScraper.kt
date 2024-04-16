package com.wechantloup.screenscraperapi.lib

public object ScreenScraper {

    private var devId: String? = null
    private var devPassword: String? = null
    private var softName: String? = null
    private var userId: String? = null
    private var userPassword: String? = null

    private var registered: Boolean = false

    public fun register(
        devId: String,
        devPassword: String,
        softName: String,
    ) {
        this.devId = devId
        this.devPassword = devPassword
        this.softName = softName
        registered = true
        // ToDo should refresh impl
    }

    public fun logIn(
        userId: String?,
        userPassword: String?,
    ) {
        this.userId = userId?.takeIf { it.isNotBlank() }
        this.userPassword = userPassword?.takeIf { it.isNotBlank() }
    }

    public val api: ScreenScraperApi by lazy {
        if (!registered) {
            throw NotRegisteredException()
        }
        ScreenScraperApiImpl(
            devId = requireNotNull(devId),
            devPassword = requireNotNull(devPassword),
            softName =requireNotNull(softName),
            userId = userId,
            userPassword = userPassword,
        )
    }
}
