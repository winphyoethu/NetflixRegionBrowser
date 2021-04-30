package com.winphyoethu.netflixmovieregionsearch.features.main

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.util.fresco.ImageUtil
import com.winphyoethu.netflixmovieregionsearch.util.inflate

class MovieAdapter(val movieClickListener: (movie: MovieRemote) -> Unit) :
    ListAdapter<MovieRemote, MovieAdapter.MovieViewHolder>(object :
        DiffUtil.ItemCallback<MovieRemote>() {

        override fun areItemsTheSame(oldItem: MovieRemote, newItem: MovieRemote): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieRemote, newItem: MovieRemote): Boolean {
            return oldItem.id == newItem.id && oldItem.title == newItem.title
        }

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(parent.inflate(R.layout.item_movie))
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.tvMovieName.text = Html.fromHtml(movie.title)

        if (movie.img != null && movie.img.isNotEmpty()) {
            holder.sdvMovie.controller = ImageUtil.buildController(
                holder.sdvMovie.context, movie.img, 200, 320, holder.sdvMovie.controller
            ) {
                if (movie.poster != null && movie.poster.isNotEmpty())
                    holder.sdvMovie.controller = ImageUtil.buildController(
                        holder.sdvMovie.context, movie.poster, 200, 320, holder.sdvMovie.controller
                    ) { }
            }
        } else if (movie.poster != null && movie.poster.isNotEmpty()) {
            holder.sdvMovie.controller = ImageUtil.buildController(
                holder.sdvMovie.context, movie.poster, 200, 320, holder.sdvMovie.controller
            ) { }
        } else {
            holder.sdvMovie.setImageResource(R.drawable.ic_movie)
        }

    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sdvMovie = itemView.findViewById<SimpleDraweeView>(R.id.sdvMovie)
        val tvMovieName = itemView.findViewById<TextView>(R.id.tvMovieName)

        init {
            itemView.setOnClickListener {
                movieClickListener(getItem(layoutPosition))
            }
        }

    }

}