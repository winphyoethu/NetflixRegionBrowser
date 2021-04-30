package com.winphyoethu.netflixmovieregionsearch.util.fresco

import android.content.Context
import android.net.Uri
import android.util.Log
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder

object ImageUtil {

    private fun imageRequest(
        context: Context, loadUri: String?, width: Int, height: Int
    ): ImageRequest {
        return if(width > 0 || height >0) {
            ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(loadUri))
                .setProgressiveRenderingEnabled(true)
                .setResizeOptions(ResizeOptions(width, height))
                .setLowestPermittedRequestLevel(
                    if (isDownloaded(loadUri, context)) {
                        ImageRequest.RequestLevel.DISK_CACHE
                    } else {
                        ImageRequest.RequestLevel.FULL_FETCH
                    }
                )
                .build()
        } else {
            ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(loadUri))
                .setProgressiveRenderingEnabled(true)
                .setLowestPermittedRequestLevel(
                    if (isDownloaded(loadUri, context)) {
                        ImageRequest.RequestLevel.DISK_CACHE
                    } else {
                        ImageRequest.RequestLevel.FULL_FETCH
                    }
                )
                .build()
        }
    }

    fun buildController(
        context: Context, loadUri: String?, width: Int, height: Int, controller: DraweeController?,
        imageFailListener: ()-> Unit
    ): DraweeController {
        return Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequest(context, loadUri, width, height))
            .setControllerListener(MyControllerListener(imageFailListener))
            .setOldController(controller)
            .build()
    }

    private fun isDownloaded(loadUri: String?, context: Context): Boolean {
        if (loadUri == null) {
            return false
        }

        val imageRequest = ImageRequest.fromUri(Uri.parse(loadUri))
        val cacheKey =
            DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, context)

        return ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey)
    }

    private fun imageRequestId(id: Int): ImageRequest {
        return ImageRequestBuilder.newBuilderWithResourceId(id)
            .setProgressiveRenderingEnabled(true)
            .disableMemoryCache()
            .build()
    }

    fun buildControllerId(id: Int, controller: DraweeController?): DraweeController {
        return Fresco.newDraweeControllerBuilder()
            .setImageRequest(imageRequestId(id))
            .setOldController(controller)
            .build()
    }

}