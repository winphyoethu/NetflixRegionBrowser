package com.winphyoethu.netflixmovieregionsearch.di

import android.app.Application
import com.winphyoethu.netflixmovieregionsearch.app.MovieSearchApp
import com.winphyoethu.netflixmovieregionsearch.di.module.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, ActivityBuilder::class, DatabaseModule::class, ViewModelModule::class, BroadcastReceiverModule::class])
interface AppComponent : AndroidInjector<MovieSearchApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: Application): Builder

        fun build(): AppComponent

    }

    override fun inject(instance: MovieSearchApp?)
}