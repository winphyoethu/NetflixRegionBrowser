package com.winphyoethu.netflixmovieregionsearch.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.winphyoethu.netflixmovieregionsearch.features.detail.MovieDetailViewModel
import com.winphyoethu.netflixmovieregionsearch.features.viewmodelfactory.ViewModelFactory
import com.winphyoethu.netflixmovieregionsearch.features.main.ui.savedmovie.SavedMovieViewModel
import com.winphyoethu.netflixmovieregionsearch.features.main.ui.home.HomeViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.FUNCTION
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
interface ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailViewModel(movieDetailViewModel: MovieDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SavedMovieViewModel::class)
    abstract fun bindSavedMovieViewModel(savedMovieViewModel: SavedMovieViewModel): ViewModel

}