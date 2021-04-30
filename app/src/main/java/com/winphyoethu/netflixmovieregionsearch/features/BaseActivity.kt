package com.winphyoethu.netflixmovieregionsearch.features

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.winphyoethu.netflixmovieregionsearch.util.gone
import com.winphyoethu.netflixmovieregionsearch.util.network.GlobalNetworkState
import com.winphyoethu.netflixmovieregionsearch.util.visible
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var globalNetworkStateSubject: PublishSubject<GlobalNetworkState>

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val fadeOut = AlphaAnimation(1f, 0f).apply {
        interpolator = DecelerateInterpolator()
        startOffset = 1500
        duration = 1500
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
    }

    abstract fun getLayoutId(): Int

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    fun goneView(vararg view: View) {
        view.forEach {
            it.gone()
        }
    }

    fun showView(vararg view: View) {
        view.forEach {
            it.visible()
        }
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun textAndAnimation(tvNetworkStatus: TextView, status: String, iconId: Int?) {
        tvNetworkStatus.apply {
            visible()
            text = status
            iconId?.let {
                setCompoundDrawablesRelativeWithIntrinsicBounds(iconId, 0, 0, 0)
            }
            animation = fadeOut
        }
        fadeOut.start()

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                tvNetworkStatus.gone()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
    }
}