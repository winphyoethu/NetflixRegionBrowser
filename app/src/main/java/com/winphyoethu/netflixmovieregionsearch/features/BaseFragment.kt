package com.winphyoethu.netflixmovieregionsearch.features

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.winphyoethu.netflixmovieregionsearch.util.gone
import com.winphyoethu.netflixmovieregionsearch.util.visible
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment : DaggerFragment(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    abstract fun getLayoutId(): Int

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

}