package com.winphyoethu.netflixmovieregionsearch.util.rx

import io.reactivex.Scheduler

interface Async {

    fun io(): Scheduler

    fun main(): Scheduler

}