package com.winphyoethu.netflixmovieregionsearch.features.main

import com.winphyoethu.netflixmovieregionsearch.features.main.ui.about.AboutFragment
import com.winphyoethu.netflixmovieregionsearch.features.main.ui.savedmovie.SavedMovieFragment
import com.winphyoethu.netflixmovieregionsearch.features.main.ui.home.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface MainBindingModule {

    @ContributesAndroidInjector
    fun providesHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    fun providesDashboardFragment(): SavedMovieFragment

    @ContributesAndroidInjector
    fun providesBottomSheetFilter(): BottomSheetFilter

    @ContributesAndroidInjector
    fun providesAboutFragment(): AboutFragment

}