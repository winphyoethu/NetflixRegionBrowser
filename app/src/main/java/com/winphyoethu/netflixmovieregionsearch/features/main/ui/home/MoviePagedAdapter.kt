package com.winphyoethu.netflixmovieregionsearch.features.main.ui.home

import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.util.fresco.ImageUtil
import com.winphyoethu.netflixmovieregionsearch.util.inflate
import com.winphyoethu.netflixmovieregionsearch.util.visible

class MoviePagedAdapter(
    val movieClickListener: (movie: MovieRemote) -> Unit, val retryClickListener: () -> Unit
) :
    PagedListAdapter<MovieRemote, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<MovieRemote>() {
        override fun areItemsTheSame(oldItem: MovieRemote, newItem: MovieRemote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieRemote, newItem: MovieRemote): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
        }
    }) {

    private var currentNetworkState: HomeViewState = Loading

    private val extraRow: Int
        get() = if (hasExtraRow())
            1
        else
            0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_loading -> LoadingViewHolder(parent.inflate(R.layout.item_loading))
            R.layout.item_movie -> MovieViewHolder(parent.inflate(R.layout.item_movie))
            else -> ErrorViewHolder(parent.inflate(R.layout.item_error_and_retry))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                if (getItem(position) != null) {
                    val movie = getItem(position)
                    movie?.let {
                        holder.tvMovieName.text = Html.fromHtml(movie.title)

                        if (movie.img != null && movie.img.isNotEmpty()) {
                            holder.sdvMovie.controller = ImageUtil.buildController(
                                holder.sdvMovie.context, movie.img, 200, 320,
                                holder.sdvMovie.controller
                            ) {
                                if (movie.poster != null && movie.poster.isNotEmpty())
                                    holder.sdvMovie.controller = ImageUtil.buildController(
                                        holder.sdvMovie.context, movie.poster, 200, 320,
                                        holder.sdvMovie.controller
                                    ) { }
                            }
                        } else if (movie.poster != null && movie.poster.isNotEmpty()) {
                            holder.sdvMovie.controller = ImageUtil.buildController(
                                holder.sdvMovie.context, movie.poster, 200, 320,
                                holder.sdvMovie.controller
                            ) { }
                        } else {
                            holder.sdvMovie.setImageResource(R.drawable.ic_movie)
                        }
                    }
                }
            }
            is LoadingViewHolder -> {

            }
            else -> {
                holder as ErrorViewHolder
                if (currentNetworkState is NextError) {
                    holder.tvErrorMessage.text = (currentNetworkState as NextError).message
                    holder.btnRetry.visible()
                } else if (currentNetworkState is NextEmpty) {
                    holder.tvErrorMessage.text = (currentNetworkState as NextEmpty).message
                }
            }
        }
    }

    override fun getItemCount(): Int {
        Log.i("EXTRAROW :: ", extraRow.toString() + " SHIT")
        return super.getItemCount() + extraRow
    }

    override fun getItemViewType(position: Int): Int {
        if (hasExtraRow() && position == itemCount - 1) {
            if (currentNetworkState == NextLoading) {
                return R.layout.item_loading
            } else if (currentNetworkState is NextError || currentNetworkState is NextEmpty) {
                return R.layout.item_error_and_retry
            }
        }
        return R.layout.item_movie
    }

    fun addHomeViewState(newHomeViewState: HomeViewState) {
        Log.i("kindofstate :: ", newHomeViewState.toString())
        val previousNetworkState = currentNetworkState
        val hadExtraRow = hasExtraRow()
        currentNetworkState = newHomeViewState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else {
            notifyItemChanged(super.getItemCount())
        }
    }

    private fun hasExtraRow(): Boolean {
        return currentNetworkState == NextLoading || currentNetworkState is NextError || currentNetworkState is NextEmpty
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sdvMovie = itemView.findViewById<SimpleDraweeView>(R.id.sdvMovie)
        val tvMovieName = itemView.findViewById<TextView>(R.id.tvMovieName)

        init {
            itemView.setOnClickListener {
                movieClickListener(getItem(layoutPosition)!!)
            }
        }

    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    inner class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvErrorMessage: TextView = itemView.findViewById(R.id.tvErrorMessage)
        val btnRetry: Button = itemView.findViewById(R.id.btnRetry)

        init {
            btnRetry.setOnClickListener {
                retryClickListener()
            }
        }

    }

}