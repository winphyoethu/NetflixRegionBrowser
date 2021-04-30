package com.winphyoethu.netflixmovieregionsearch.features.main.ui.home

sealed class NetworkState
data class NetworkError(val message: String?) : NetworkState()
object NetworkLoading : NetworkState()
object NetworkEmpty : NetworkState()
object InitialEmpty : NetworkState()