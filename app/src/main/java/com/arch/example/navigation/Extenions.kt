package com.arch.example.navigation

import androidx.navigation.NavDestination

val NavDestination.routeWithoutArgs: String?
    get() = route
        ?.substringBefore("/")
        ?.substringBefore("?")