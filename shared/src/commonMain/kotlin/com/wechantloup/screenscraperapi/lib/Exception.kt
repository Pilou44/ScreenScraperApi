package com.wechantloup.screenscraperapi.lib

public class BadDevIdsException(cause: Exception): Exception(cause)
public class MissingUrlParameterException(cause: Exception): Exception(cause)

public class NotRegisteredException: Exception("Dev account should be registered")
