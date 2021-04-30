package com.winphyoethu.netflixmovieregionsearch.features.main.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import com.winphyoethu.netflixmovieregionsearch.R
import kotlinx.android.synthetic.main.activity_library.*

class LibraryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Used Libraries"

        wvUsedLibraries.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        wvUsedLibraries.loadUrl("file:///android_asset/libraries.html")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}