package com.winphyoethu.netflixmovieregionsearch.app

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.multidex.MultiDex
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.winphyoethu.netflixmovieregionsearch.di.DaggerAppComponent
import com.winphyoethu.netflixmovieregionsearch.util.network.NetworkChangeReceiver
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

const val MAX_DISK_CACHE_SIZE = 150L * ByteConstants.MB

class MovieSearchApp : Application(), HasAndroidInjector, LifecycleObserver {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    private val intentFilter: IntentFilter by lazy {
        IntentFilter().apply {
            this.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            this.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        }
    }

    private val networkChangeReceiver: NetworkChangeReceiver by lazy {
        NetworkChangeReceiver()
    }

    override fun onCreate() {
        super.onCreate()

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

        val frescoPipeline = ImagePipelineConfig.newBuilder(this)
            .setMainDiskCacheConfig(
                DiskCacheConfig.newBuilder(this)
                    .setBaseDirectoryPath(cacheDir)
                    .setBaseDirectoryName("movieSearch")
                    .setMaxCacheSize(MAX_DISK_CACHE_SIZE)
                    .build()
            )
            .setDiskCacheEnabled(true)
            .setDownsampleEnabled(true)
            .build()

        Fresco.initialize(this, frescoPipeline)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        unregisterReceiver(networkChangeReceiver)
    }

}