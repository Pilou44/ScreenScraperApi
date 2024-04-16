package com.wechantloup.screenscraperapi.lib

public class BadDevIdsException(e: Exception): Exception(e)

public class NotRegisteredException: Exception("Dev account should be registered")
