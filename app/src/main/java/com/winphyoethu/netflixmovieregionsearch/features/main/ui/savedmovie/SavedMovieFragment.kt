package com.winphyoethu.netflixmovieregionsearch.features.main.ui.savedmovie

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.features.BaseFragment
import com.winphyoethu.netflixmovieregionsearch.features.detail.IS_FROM_SAVED
import com.winphyoethu.netflixmovieregionsearch.features.detail.MOVIE_ID
import com.winphyoethu.netflixmovieregionsearch.features.detail.MovieDetailActivity
import com.winphyoethu.netflixmovieregionsearch.features.main.MovieAdapter
import kotlinx.android.synthetic.main.fragment_saved_movie.*
import javax.inject.Inject

class SavedMovieFragment : BaseFragment() {

    private lateinit var savedMovieViewModel: SavedMovieViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter {
            val intent = Intent(requireActivity(), MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_ID, it.netflixId)
            intent.putExtra(IS_FROM_SAVED, true)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSavedMovie.apply {
            layoutManager = GridLayoutManager(this.context, 3)
            adapter = movieAdapter
        }

        savedMovieViewModel = ViewModelProvider(this, viewModelFactory).get(SavedMovieViewModel::class.java)

        compositeDisposable.add(
            savedMovieViewModel.savedMovieSubject
                .subscribe({
                    when(it) {
                        is Show -> {
                            goneView(pbSavedMovie, tvErrorMessage)
                            showView(rvSavedMovie)
                            movieAdapter.submitList(it.movieList)
                        }
                        is Empty -> {
                            goneView(pbSavedMovie, rvSavedMovie)
                            showView(tvErrorMessage)
                            tvErrorMessage.text = it.message
                        }
                        Loading -> {
                            goneView(tvErrorMessage, rvSavedMovie)
                            showView(pbSavedMovie)
                        }
                        is Error -> {
                            goneView(pbSavedMovie, rvSavedMovie)
                            showView(tvErrorMessage)
                            tvErrorMessage.text = it.message
                        }
                    }
                }, {

                })
        )
    }

    override fun onResume() {
        super.onResume()

        savedMovieViewModel.getMovie()
    }

    override fun getLayoutId(): Int = R.layout.fragment_saved_movie

}