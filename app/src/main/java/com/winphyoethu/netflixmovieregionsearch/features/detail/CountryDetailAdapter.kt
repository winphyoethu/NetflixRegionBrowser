package com.winphyoethu.netflixmovieregionsearch.features.detail

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.CountryDetail
import com.winphyoethu.netflixmovieregionsearch.util.fresco.ImageUtil
import com.winphyoethu.netflixmovieregionsearch.util.inflate

class CountryDetailAdapter :
    ListAdapter<CountryDetail, CountryDetailAdapter.CountryDetailViewHolder>(
        object : DiffUtil.ItemCallback<CountryDetail>() {
            override fun areItemsTheSame(oldItem: CountryDetail, newItem: CountryDetail): Boolean {
                return oldItem.cc == newItem.cc
            }

            override fun areContentsTheSame(
                oldItem: CountryDetail, newItem: CountryDetail
            ): Boolean {
                return oldItem.cc == newItem.cc && oldItem.country == newItem.country
            }
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryDetailViewHolder {
        return CountryDetailViewHolder(parent.inflate(R.layout.item_country_detail))
    }

    override fun onBindViewHolder(holder: CountryDetailViewHolder, position: Int) {
        val countryDetail = getItem(position)

        val flagUrl = "https://www.countryflags.io/" + countryDetail.cc + "/flat/64.png"

        holder.tvAvailableAudio.text = countryDetail.audio
        holder.tvAvailableSubtitle.text = countryDetail.subtitle
        holder.tvAvailableRegion.text = countryDetail.country

        holder.sdvAvailableCountryFlag.controller = ImageUtil.buildController(
            holder.sdvAvailableCountryFlag.context, flagUrl, 0, 0,
            holder.sdvAvailableCountryFlag.controller
        ) { }
    }

    inner class CountryDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sdvAvailableCountryFlag: SimpleDraweeView =
            itemView.findViewById(R.id.sdvAvailableCountryFlag)
        val tvAvailableRegion: TextView = itemView.findViewById(R.id.tvAvailableRegion)
        val tvAvailableSubtitle: TextView = itemView.findViewById(R.id.tvAvailableSubtitle)
        val tvAvailableAudio: TextView = itemView.findViewById(R.id.tvAvailableAudio)

    }

}