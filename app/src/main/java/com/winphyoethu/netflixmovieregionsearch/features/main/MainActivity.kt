package com.winphyoethu.netflixmovieregionsearch.features.main

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.features.BaseActivity
import com.winphyoethu.netflixmovieregionsearch.util.network.Mobile
import com.winphyoethu.netflixmovieregionsearch.util.network.Wifi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()

        compositeDisposable.add(
            globalNetworkStateSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it) {
                        Wifi -> textAndAnimation(
                            tvNetworkStatus, getString(R.string.label_connected_to_internet), R.drawable.ic_connected
                        )
                        Mobile -> textAndAnimation(
                            tvNetworkStatus, getString(R.string.label_connected_to_internet), R.drawable.ic_connected
                        )
                        else -> textAndAnimation(
                            tvNetworkStatus, getString(R.string.label_disconnected_to_internet),
                            R.drawable.ic_disconnected
                        )
                    }
                }, {

                })
        )

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.i("currtoken :: ", it)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

}