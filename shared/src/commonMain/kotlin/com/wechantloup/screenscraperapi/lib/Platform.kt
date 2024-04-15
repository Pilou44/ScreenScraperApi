package com.wechantloup.screenscraperapi.lib

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
