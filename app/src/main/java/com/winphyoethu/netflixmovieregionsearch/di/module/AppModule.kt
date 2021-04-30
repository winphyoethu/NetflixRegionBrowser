package com.winphyoethu.netflixmovieregionsearch.di.module

import android.app.Application
import android.util.Log
import com.readystatesoftware.chuck.ChuckInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.winphyoethu.netflixmovieregionsearch.data.local.LocalRepository
import com.winphyoethu.netflixmovieregionsearch.data.local.LocalRepositoryImpl
import com.winphyoethu.netflixmovieregionsearch.data.remote.ApiService
import com.winphyoethu.netflixmovieregionsearch.data.remote.RemoteRepository
import com.winphyoethu.netflixmovieregionsearch.data.remote.RemoteRepositoryImpl
import com.winphyoethu.netflixmovieregionsearch.util.network.GlobalNetworkState
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import com.winphyoethu.netflixmovieregionsearch.util.rx.AsyncManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [AppModule.DeclareBind::class])
class AppModule {

    @Named("baseUrl")
    @Provides
    fun providesBaseUrl(): String = "https://unogsng.p.rapidapi.com/"

    @Named("headerInterceptor")
    @Provides
    fun providesHeaderInterceptor(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()

                val cacheControl = CacheControl.Builder()
                    .maxAge(10, TimeUnit.HOURS)
                    .build()

                val newRequest = request.newBuilder()
                    .addHeader(
                        "x-rapidapi-key",
                        "9d9c2b1e06msh42e05ecd5f8f6a8p16add4jsnaaede2524043"
                    )
                    .addHeader("x-rapidapi-host", "unogsng.p.rapidapi.com")
                    .addHeader("useQueryString", "true")
                    .addHeader("Cache-Control", cacheControl.toString())
                    .method(request.method, request.body)
                    .build()

                return chain.proceed(newRequest)
            }
        }
    }

//    @Named("offlineCacheInterceptor")
//    @Provides
//    fun providesOfflineCacheInterceptor(): Interceptor {
//        return object : Interceptor {
//            override fun intercept(chain: Interceptor.Chain): Response {
//                val requestBuilder = chain.request().newBuilder()
//
//                requestBuilder.cacheControl(CacheControl.FORCE_CACHE)
//
//                return chain.proceed(requestBuilder.build())
//            }
//        }
//    }

    @Provides
    fun providesOkHttp(
        @Named("headerInterceptor") headerInterceptor: Interceptor,
        app: Application
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(ChuckInterceptor(app))
            .addInterceptor(headerInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .proxy(Proxy.NO_PROXY)
            .build()
    }

    @Provides
    fun providesMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Provides
    fun providesRetrofit(
        @Named("baseUrl") baseUrl: String, okHttpClient: OkHttpClient, moshi: Moshi
    ): Retrofit {
        Log.i("okclinet :: ", okHttpClient.hashCode().toString() + " YES")
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesNetworkState(): PublishSubject<GlobalNetworkState> {
        return PublishSubject.create()
    }

    @Module
    interface DeclareBind {

        @Binds
        fun bindsRemoteRepository(remoteRepository: RemoteRepositoryImpl): RemoteRepository

        @Binds
        fun bindsLocalRepository(localRepository: LocalRepositoryImpl): LocalRepository

        @Binds
        fun bindAsyncManager(asyncManager: AsyncManager): Async

    }

}