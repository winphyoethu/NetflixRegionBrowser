package com.winphyoethu.netflixmovieregionsearch.features.main

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.changes
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.features.main.ui.CountryFilterAdapter
import com.winphyoethu.netflixmovieregionsearch.features.main.ui.home.HomeViewModel
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import javax.inject.Inject

class BottomSheetFilter : BottomSheetDialogFragment(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var async: Async

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val countryFilterAdapter: CountryFilterAdapter by lazy {
        CountryFilterAdapter({ isSelected, position ->
            homeViewModel.setSpecificCountrySelected(position, isSelected)
        }, {
            Toast.makeText(requireContext(), it.country, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(context, R.layout.bottom_sheet_filter, null)

        bottomSheet.setContentView(view)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = getWindowHeight()

        return bottomSheet
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View.inflate(context, R.layout.bottom_sheet_filter, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(HomeViewModel::class.java)

        rvFilterCountry.apply {
            layoutManager = GridLayoutManager(this.context, 7)
            adapter = countryFilterAdapter
        }

        homeViewModel.checkCountries()
        homeViewModel.observeFilter()

        compositeDisposable.add(
            homeViewModel.filterSubject
                .observeOn(async.main())
                .subscribe({
                    if (it.movieType == "movie") {
                        rbMovie.isChecked = true
                    } else {
                        rbSeries.isChecked = true
                    }
                    tvYearStart.text = it.startYear
                    tvImdbRatingStart.text = it.startRating

                    sbImdbRating.progress = it.startRating.toInt()
                    sbYearRange.progress = it.startYear.toInt() - 1900

                    countryFilterAdapter.setCustomItem(it.selectedCountriesList)
                }, {
                    Log.i("SUBERROR :: ", it.message.toString())
                })
        )

        compositeDisposable.add(
            homeViewModel.countrySubject.subscribe({
                countryFilterAdapter.submitList(it)
            }, {

            })
        )

        cbCountryCheckAll.checkedChanges()
            .skip(1)
            .subscribe({
                countryFilterAdapter.setAllCheck(it)
                homeViewModel.setAllCountrySelected(it)
            }, {

            })

        sbImdbRating.changes()
            .skip(1)
            .subscribe({
                tvImdbRatingStart.text = it.toString()
                homeViewModel.setImdbRating(it.toString())
            }, {

            })

        sbYearRange.changes()
            .skip(1)
            .subscribe({
                tvYearStart.text = (it + 1900).toString()
                homeViewModel.setYear((it + 1900).toString())
            }, {

            })

        rgMovieOrSeries.checkedChanges()
            .skip(1)
            .subscribe({
                if (it == R.id.rbMovie) {
                    homeViewModel.setMovieOrSeries("movie")
                } else if (it == R.id.rbSeries) {
                    homeViewModel.setMovieOrSeries("series")
                }
            }, {

            })

        btnFilter.clicks()
            .subscribe({
                dismiss()
                homeViewModel.invalidate()
            }, {

            })

        ivClose.clicks()
            .subscribe({
                dismiss()
            }, {

            })
    }

    private fun getWindowHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}