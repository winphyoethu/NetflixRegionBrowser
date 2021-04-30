package com.winphyoethu.netflixmovieregionsearch.features.detail

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding4.widget.itemSelections
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.features.BaseActivity
import com.winphyoethu.netflixmovieregionsearch.util.fresco.ImageUtil
import com.winphyoethu.netflixmovieregionsearch.util.gone
import com.winphyoethu.netflixmovieregionsearch.util.network.Mobile
import com.winphyoethu.netflixmovieregionsearch.util.network.Wifi
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import com.winphyoethu.netflixmovieregionsearch.util.visible
import kotlinx.android.synthetic.main.activity_movie_detail.*
import javax.inject.Inject

const val MOVIE_ID = "movie_id"
const val IS_FROM_SAVED = "is_from_saved"

class MovieDetailActivity : BaseActivity() {

    private var netflixId: Int = 0
    private var isFromSaved: Boolean = false
    private var isSaved: Boolean = false

    lateinit var movieDetailViewModel: MovieDetailViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var async: Async

    private val countryDetailAdapter: CountryDetailAdapter by lazy {
        CountryDetailAdapter()
    }

    private val episodeAdapter: EpisodeAdapter by lazy {
        EpisodeAdapter { episode, position ->
            val builder = AlertDialog.Builder(this, R.style.MyDialogStyle)
            builder.setTitle("Ep ${position + 1} : ${episode.title}")
                .setMessage(Html.fromHtml(episode.synopsis))
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }
    }

    private lateinit var seasonAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        ctlMovieDetail.title = ""
        ctlMovieDetail.setExpandedTitleColor(Color.parseColor("#00000000"))
        ctlMovieDetail.setCollapsedTitleTextColor(Color.parseColor("#00000000"))

        rvMovieAvailableRegion.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = countryDetailAdapter
            isNestedScrollingEnabled = false
        }

        rvEpisodes.apply {
            layoutManager = GridLayoutManager(this.context, 2, GridLayoutManager.HORIZONTAL, false)
            adapter = episodeAdapter
        }

        movieDetailViewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel::class.java)

        netflixId = intent.getIntExtra(MOVIE_ID, 0)
        isFromSaved = intent.getBooleanExtra(IS_FROM_SAVED, false)

        movieDetailViewModel.getMovieDetail(netflixId, isFromSaved)

        btnWatchOnNetflix.setOnClickListener {
            val watchUrl = "http://www.netflix.com/watch/$netflixId"

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setClassName("com.netflix.mediaclient", "com.netflix.mediaclient.ui.launch.UIWebViewActivity")
                intent.data = Uri.parse(watchUrl)
                startActivity(intent)
            } catch (e: Exception) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(watchUrl)
                startActivity(intent)
            }

        }

        tvSaveMovie.setOnClickListener {
            if (!btnRetry.isVisible) {
                if (isSaved) {
                    movieDetailViewModel.deleteMovie()
                } else {
                    movieDetailViewModel.saveMovie()
                }
            } else {
                val builder = AlertDialog.Builder(this).apply {
                    title = "Warning"
                    setMessage("No data to save")
                    setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        spSeason.itemSelections()
            .subscribe({
                if (it >= 0) {
                    movieDetailViewModel.getEpisode(it)
                }
            }, {

            })

        btnRetry.setOnClickListener {
            movieDetailViewModel.getMovieDetail(netflixId, isFromSaved)
        }
    }

    override fun onResume() {
        super.onResume()

        observeNetworkState()

        observeMovieDetailState()
    }

    override fun getLayoutId(): Int = R.layout.activity_movie_detail

    private fun observeNetworkState() {
        compositeDisposable.add(
            globalNetworkStateSubject
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    when (it) {
                        Wifi -> {
                            textAndAnimation(
                                tvNetworkStatus,
                                getString(R.string.label_connected_to_internet),
                                R.drawable.ic_connected
                            )
                            if (!isFromSaved && tvMovieType.text == "type") {
                                pbMovieDetail.visible()
                                btnRetry.gone()
                                movieDetailViewModel.getMovieDetail(netflixId, isFromSaved)
                                textAndAnimation(
                                    tvNetworkStatus, getString(R.string.label_getting_movie_again),
                                    R.drawable.ic_connected
                                )
                            }
                        }
                        Mobile -> {
                            textAndAnimation(
                                tvNetworkStatus,
                                getString(R.string.label_connected_to_internet),
                                R.drawable.ic_connected
                            )
                            if (!isFromSaved && tvMovieType.text == "type") {
                                pbMovieDetail.visible()
                                btnRetry.gone()
                                movieDetailViewModel.getMovieDetail(netflixId, isFromSaved)
                                textAndAnimation(
                                    tvNetworkStatus, getString(R.string.label_getting_movie_again),
                                    R.drawable.ic_connected
                                )
                            }
                        }
                        else -> textAndAnimation(
                            tvNetworkStatus, getString(R.string.label_disconnected_to_internet),
                            R.drawable.ic_disconnected
                        )
                    }
                }, {

                })
        )
    }

    private fun observeMovieDetailState() {
        compositeDisposable.add(
            movieDetailViewModel.movieDetailSubject
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    when (it) {
                        is ShowMovieDetail -> {
                            goneView(pbMovieDetail, btnRetry)

                            val movie = it.movieDetail
                            tvMoviePlot.text = Html.fromHtml(movie.imdbPlot)
                            tvMovieSynopsis.text = Html.fromHtml(movie.synopsis)
                            tvMovieRunTime.text = "${movie.imdbRuntime}"
                            tvMovieGenre.text = "${movie.imdbGenre}"
                            tvMovieType.text = movie.vType
                            tvMovieTitle.text =
                                String.format(getString(R.string.label_movie_year), movie.title, movie.year)
                            tvMovieRating.text = String.format(getString(R.string.label_rating), movie.avgRating)
                            if (!movie.matLabel.isNullOrEmpty()) {
                                tvMovieMaturity.text = movie.matLabel
                            } else {
                                goneView(tvMovieMaturity, tvMovieMaturityHeader)
                            }

                            if (movie.image != null) {
                                sdvMovieDetail.controller = ImageUtil.buildController(
                                    sdvMovieDetail.context, movie.image, 0, 0, sdvMovieDetail.controller
                                ) {
                                    sdvMovieDetail.controller = ImageUtil.buildController(
                                        sdvMovieDetail.context, movie.imdbPoster, 0, 0, sdvMovieDetail.controller
                                    ) { }
                                }
                            } else {
                                sdvMovieDetail.controller = ImageUtil.buildController(
                                    sdvMovieDetail.context, movie.imdbPoster, 0, 0, sdvMovieDetail.controller
                                ) { }
                            }
                        }
                        is ShowCountries -> {
                            countryDetailAdapter.submitList(it.countryDetail)
                        }
                        is ShowSeasons -> {
                            val seasons = it.season
                            episodeAdapter.submitList(seasons[0].episodeList)

                            val seasonList: MutableList<String> = ArrayList()
                            seasons.forEach { season ->
                                seasonList.add("Season ${season.season}")
                            }

                            showView(spSeason, rvEpisodes)

                            seasonAdapter = ArrayAdapter(this, R.layout.item_spinner_episode)
                            seasonAdapter.setDropDownViewResource(R.layout.item_spinner_episode)
                            spSeason.adapter = seasonAdapter
                            seasonAdapter.addAll(seasonList)
                        }
                        is ShowEpisodes -> {
                            episodeAdapter.submitList(null)
                            episodeAdapter.submitList(it.episodes)
                        }
                        is ShowIsSavedMovie -> {
                            isSaved = it.boolean
                            if (it.boolean) {
                                Toast.makeText(this, getString(R.string.toast_movie_saved), Toast.LENGTH_SHORT).show()
                                setIsSavedOrNot(getString(R.string.label_saved), R.drawable.ic_bookmark)
                            } else {
                                Toast.makeText(this, getString(R.string.toast_movie_save), Toast.LENGTH_SHORT).show()
                                setIsSavedOrNot(getString(R.string.label_save), R.drawable.ic_bookmark_empty)
                            }
                        }
                        is CheckSavedMovie -> {
                            isSaved = it.boolean
                            if (it.boolean) {
                                setIsSavedOrNot(getString(R.string.label_saved), R.drawable.ic_bookmark)
                            } else {
                                setIsSavedOrNot(getString(R.string.label_save), R.drawable.ic_bookmark_empty)
                            }
                        }
                        is ShowError -> {
                            pbMovieDetail.gone()
                            btnRetry.visible()
                            textAndAnimation(tvNetworkStatus, it.message, null)
                        }
                    }
                }, {

                })
        )
    }

    private fun setIsSavedOrNot(text: String, iconId: Int) {
        tvSaveMovie.text = text
        tvSaveMovie.setCompoundDrawablesRelativeWithIntrinsicBounds(iconId, 0, 0, 0)
    }

}