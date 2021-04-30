package com.winphyoethu.netflixmovieregionsearch.features.main.ui.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.winphyoethu.netflixmovieregionsearch.BuildConfig
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.util.fresco.ImageUtil
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sdvLogo.controller = ImageUtil.buildController(
            sdvLogo.context,
            "https://cdn.vox-cdn.com/thumbor/QuS2QKQys3HhosKiV-2IuKhphbo=/39x0:3111x2048/1400x1050/filters:focal(39x0:3111x2048):format(png)/cdn.vox-cdn.com/uploads/chorus_image/image/49901753/netflixlogo.0.0.png",
            200, 200, sdvLogo.controller
        ) {
            Log.i("sdvfail :: ", "yes it is")
        }

        tvRateApplication.setOnClickListener {
            val appPackageName = "com.winphyoethu.higherlowergame"

            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
        }

        tvLibraries.setOnClickListener {
            val intent = Intent(requireActivity(), LibraryActivity::class.java)
            startActivity(intent)
        }

        tvShare.setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
                )
            }
            startActivity(Intent.createChooser(sendIntent, "choose one"))
        }
    }

}