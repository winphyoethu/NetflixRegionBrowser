package com.winphyoethu.netflixmovieregionsearch.features.detail

import android.text.Html
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.EpisodeModel
import com.winphyoethu.netflixmovieregionsearch.util.inflate

class EpisodeAdapter(val episodeClickListener: (episode: EpisodeModel, position: Int) -> Unit) :
    ListAdapter<EpisodeModel, EpisodeAdapter.EpisodeViewHolder>(object :
        DiffUtil.ItemCallback<EpisodeModel>() {
        override fun areItemsTheSame(oldItem: EpisodeModel, newItem: EpisodeModel): Boolean {
            return oldItem.episodeId == newItem.episodeId
        }

        override fun areContentsTheSame(oldItem: EpisodeModel, newItem: EpisodeModel): Boolean {
            return oldItem == newItem
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(parent.inflate(R.layout.item_episode))
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = getItem(position)

        holder.tvEpisodeTitle.text = Html.fromHtml("Ep ${position + 1} : " + episode.title)
    }

    inner class EpisodeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvEpisodeTitle: TextView = itemView.findViewById(R.id.tvEpisodeTitle)

        init {
            itemView.setOnClickListener {
                episodeClickListener(getItem(layoutPosition), layoutPosition)
            }
        }

    }

}