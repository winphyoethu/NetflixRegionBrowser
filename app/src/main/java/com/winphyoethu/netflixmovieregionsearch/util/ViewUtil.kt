package com.winphyoethu.netflixmovieregionsearch.util

import android.view.View

object ViewUtil {

    fun viewGone(vararg view: View) {
        view.iterator().forEach {
            it.gone()
        }
    }

    fun viewVisible(vararg view: View) {
        view.iterator().forEach {
            it.visible()
        }
    }

}