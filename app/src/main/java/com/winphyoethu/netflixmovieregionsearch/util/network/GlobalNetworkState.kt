package com.winphyoethu.netflixmovieregionsearch.util.network

sealed class GlobalNetworkState
object Wifi : GlobalNetworkState()
object Mobile : GlobalNetworkState()
object Disconnected : GlobalNetworkState()