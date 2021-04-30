package com.winphyoethu.netflixmovieregionsearch.util.fresco

import android.util.Log
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo

class MyControllerListener(val imageFailListener: ()-> Unit) : BaseControllerListener<ImageInfo>() {

    override fun onFailure(id: String?, throwable: Throwable?) {
        super.onFailure(id, throwable)
        Log.i("ImageFailURE :: ", throwable?.message.toString()+ " :: "+ id)
        imageFailListener()
    }

}