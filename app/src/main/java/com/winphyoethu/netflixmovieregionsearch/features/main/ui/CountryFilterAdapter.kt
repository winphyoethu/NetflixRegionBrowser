package com.winphyoethu.netflixmovieregionsearch.features.main.ui

import android.util.Log
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import com.winphyoethu.netflixmovieregionsearch.R
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Country
import com.winphyoethu.netflixmovieregionsearch.util.fresco.ImageUtil
import com.winphyoethu.netflixmovieregionsearch.util.gone
import com.winphyoethu.netflixmovieregionsearch.util.inflate
import com.winphyoethu.netflixmovieregionsearch.util.visible

class CountryFilterAdapter(
    val countrySelected: (isSelected: Boolean, position: Int) -> Unit,
    val longCLick: (country: Country) -> Unit
) : ListAdapter<Country, CountryFilterAdapter.CountryViewHolder>(object :
    DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.id == newItem.id && oldItem.countryCode == newItem.countryCode
    }
}) {

    val selectedAry: SparseBooleanArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(parent.inflate(R.layout.item_country_filter))
    }

    override fun onBindViewHolder(
        holder: CountryViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            val country = getItem(position)

            val flagUrl = "https://www.countryflags.io/" + country.countryCode + "/flat/64.png"

            holder.sdvCountryFilter.controller = ImageUtil.buildController(
                holder.sdvCountryFilter.context, flagUrl, 0, 0, holder.sdvCountryFilter.controller
            ) { }

            if (selectedAry[position]) {
                holder.ivCountrySelected.visible()
            } else {
                holder.ivCountrySelected.gone()
            }
        } else {
            if (payloads[0] is Boolean) {
                val select = payloads[0] as Boolean

                if (select) {
                    holder.ivCountrySelected.visible()
                } else {
                    holder.ivCountrySelected.gone()
                }
            }
        }
    }

    fun setAllCheck(isAllCheck: Boolean) {
        if (isAllCheck) {
            for (i in 0 until this.itemCount) {
                selectedAry.put(i, true)
            }
            notifyItemRangeChanged(0, itemCount, true)
        } else {
            selectedAry.clear()
            notifyItemRangeChanged(0, itemCount, false)
        }
    }

    fun setCustomItem(selectedCountry: String) {
        val countryIdAry = selectedCountry.split(",")
        countryIdAry.forEach { countryId ->
            for (i in 0 until itemCount) {
                if (currentList[i].countryId.toString() == countryId) {
                    selectedAry.put(i, true)
                    notifyItemChanged(i, true)
                    break
                }
            }
        }
    }

    inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val sdvCountryFilter: SimpleDraweeView = itemView.findViewById(R.id.sdvCountryFilter)
        val ivCountrySelected: ImageView = itemView.findViewById(R.id.ivCountrySelected)

        init {
            itemView.setOnClickListener {
                if (selectedAry[layoutPosition]) {
                    selectedAry.put(layoutPosition, false)
                    notifyItemChanged(layoutPosition, false)
                } else {
                    selectedAry.put(layoutPosition, true)
                    notifyItemChanged(layoutPosition, true)
                }
                countrySelected(selectedAry[layoutPosition], layoutPosition)
            }

            itemView.setOnLongClickListener {
                longCLick(currentList[layoutPosition])
                true
            }
        }

    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {

    }
}