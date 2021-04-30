package com.winphyoethu.netflixmovieregionsearch.di.module

import com.winphyoethu.netflixmovieregionsearch.features.detail.MovieDetailActivity
import com.winphyoethu.netflixmovieregionsearch.features.main.MainActivity
import com.winphyoethu.netflixmovieregionsearch.features.main.MainBindingModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainBindingModule::class])
    abstract fun bindHomeActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindMovieDetailActivity(): MovieDetailActivity

}