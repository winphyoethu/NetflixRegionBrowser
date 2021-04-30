package com.winphyoethu.netflixmovieregionsearch.di.module

import com.winphyoethu.netflixmovieregionsearch.util.network.NetworkChangeReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Inject

@Module
abstract class BroadcastReceiverModule {

    @ContributesAndroidInjector
    abstract fun bindNetworkChangeReceiver(): NetworkChangeReceiver

}