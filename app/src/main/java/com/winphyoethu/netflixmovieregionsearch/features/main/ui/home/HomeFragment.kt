package com.winphyoethu.netflixmovieregionsearch.features.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jakewharton.rxbinding4.appcompat.queryTextChanges
import com.jakewharton.rxbinding4.view.clicks
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.features.BaseFragment
import com.winphyoethu.netflixmovieregionsearch.features.detail.IS_FROM_SAVED
import com.winphyoethu.netflixmovieregionsearch.features.detail.MOVIE_ID
import com.winphyoethu.netflixmovieregionsearch.features.detail.MovieDetailActivity
import com.winphyoethu.netflixmovieregionsearch.util.ViewUtil
import com.winphyoethu.netflixmovieregionsearch.util.network.GlobalNetworkState
import com.winphyoethu.netflixmovieregionsearch.util.network.Mobile
import com.winphyoethu.netflixmovieregionsearch.util.network.Wifi
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    private lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var asyncManager: Async

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var globalNetworkStateSubject: PublishSubject<GlobalNetworkState>

    private val moviePagedAdapter: MoviePagedAdapter by lazy {
        MoviePagedAdapter({
            val intent = Intent(requireActivity(), MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_ID, it.netflixId)
            intent.putExtra(IS_FROM_SAVED, false)
            startActivity(intent)
        }, {
            homeViewModel.retryNext()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        homeViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvMovie.apply {
            layoutManager = GridLayoutManager(this.context, 3, GridLayoutManager.VERTICAL, false).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when (moviePagedAdapter.getItemViewType(position)) {
                            R.layout.item_movie -> 1
                            else -> 3
                        }
                    }
                }
            }
            adapter = moviePagedAdapter
        }

        homeViewModel.checkCountries()
        homeViewModel.observeMovie()

        ivQueryClear.clicks()
            .throttleFirst(600, TimeUnit.MILLISECONDS)
            .subscribe({
                homeViewModel.setQuery("")
                homeViewModel.invalidate()
            }, {

            })

        ivYearClear.clicks()
            .throttleFirst(600, TimeUnit.MILLISECONDS)
            .subscribe({
                homeViewModel.setYear("")
                homeViewModel.invalidate()
            }, {

            })

        ivRatingClear.clicks()
            .throttleFirst(600, TimeUnit.MILLISECONDS)
            .subscribe({
                homeViewModel.setImdbRating("")
                homeViewModel.invalidate()
            }, {

            })

        btnRetry.clicks()
            .throttleFirst(600, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribe({
                homeViewModel.invalidate()
            }, {

            })

        compositeDisposable.add(
            homeViewModel.homeViewStateSubject
                .observeOn(asyncManager.main())
                .subscribe({
                    when (it) {
                        Loading -> {
                            showView(pbProgressBar)
                            goneView(rvMovie, tvErrorMessage, btnRetry)
                        }
                        is Show -> {
                            showView(rvMovie)
                            goneView(pbProgressBar, tvErrorMessage, btnRetry)

                            moviePagedAdapter.addHomeViewState(it)
                            if (!it.movieList.isNullOrEmpty()) {
                                moviePagedAdapter.apply {
                                    submitList(null)
                                    submitList(it.movieList)
                                }
                            }
                        }
                        EmptyList -> {
                            moviePagedAdapter.submitList(null)
                        }
                        is Error -> {
                            showView(tvErrorMessage, btnRetry)
                            goneView(rvMovie, pbProgressBar)
                            tvErrorMessage.text = it.message
                        }
                        is Empty -> {
                            moviePagedAdapter.apply {
                                submitList(null)
                            }
                            showView(tvErrorMessage)
                            goneView(rvMovie, pbProgressBar, btnRetry)
                            tvErrorMessage.text = it.message
                        }
                        is NextEmpty -> {
                            moviePagedAdapter.addHomeViewState(it)
                        }
                        is NextError -> {
                            moviePagedAdapter.addHomeViewState(it)
                        }
                        is NextLoading -> {
                            moviePagedAdapter.addHomeViewState(it)
                        }
                        is Filter -> {
                            if (it.filter.query.isEmpty()) {
                                ViewUtil.viewGone(tvQuery, ivQueryClear)
                            } else {
                                ViewUtil.viewVisible(tvQuery, ivQueryClear, hsvFilter)
                                tvQuery.text = it.filter.query
                            }

                            tvType.text = it.filter.movieType

                            if (it.filter.startYear.isEmpty()) {
                                ViewUtil.viewGone(tvYear, ivYearClear)
                            } else {
                                ViewUtil.viewVisible(tvYear, hsvFilter)
                                tvYear.text = String.format(
                                    getString(R.string.label_filter_update), it.filter.startYear, it.filter.endYear
                                )
                            }

                            if (it.filter.startRating.isEmpty()) {
                                ViewUtil.viewGone(tvRating, ivRatingClear)
                            } else {
                                ViewUtil.viewVisible(tvRating, hsvFilter)
                                tvRating.text = String.format(
                                    getString(R.string.label_filter_update), it.filter.startRating, it.filter.endRating
                                )
                            }

                            if (it.filter.selectedCountries == 0) {
                                ViewUtil.viewGone(tvSelectedCountry, ivSelectedCountryClear)
                            } else {
                                ViewUtil.viewVisible(tvSelectedCountry, ivSelectedCountryClear, hsvFilter)
                                tvSelectedCountry.text =
                                    if (it.filter.selectedCountries == 1) "${it.filter.selectedCountries} Country Selected" else "${it.filter.selectedCountries} Countries Selected"
                            }
                        }
                        else -> {

                        }
                    }
                }, {

                })
        )
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)

        val movieSearchView = menu.findItem(R.id.menu_movie_search)?.actionView as SearchView

        movieSearchView.queryTextChanges()
            .filter {
                it.length > 3
            }
            .debounce(600, TimeUnit.MILLISECONDS)
            .subscribe({
                homeViewModel.setQuery(it.toString())
                homeViewModel.invalidate()
            }, {

            })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_movie_filter) {
            NavHostFragment.findNavController(this).navigate(R.id.navigation_filter)
        }
        return super.onOptionsItemSelected(item)
    }
}