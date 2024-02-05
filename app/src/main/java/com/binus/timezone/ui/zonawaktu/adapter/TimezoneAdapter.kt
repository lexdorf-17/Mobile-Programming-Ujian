package com.binus.timezone.ui.zonawaktu.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.binus.timezone.R
import com.binus.timezone.databinding.ItemListZonaBinding
import com.binus.timezone.prefs.AppPreferences
import com.binus.timezone.ui.zonawaktu.model.Timezone
import com.binus.timezone.ui.zonawaktu.view.ZoneActivity
import com.bumptech.glide.Glide

class TimezoneAdapter(data: List<Timezone>,context: Context): ListAdapter<Timezone, TimezoneAdapter.ViewHolder>(ItemDiffCallback) {
    private val mData: List<Timezone> = data
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mContext: Context = context
    var onClickItems: ((Timezone) -> Unit)? = null

    companion object ItemDiffCallback: DiffUtil.ItemCallback<Timezone>() {
        override fun areItemsTheSame(
            oldItem: Timezone,
            newItem: Timezone
        ): Boolean {
            return oldItem.timezone == newItem.timezone
        }

        override fun areContentsTheSame(
            oldItem: Timezone,
            newItem: Timezone
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemListZonaBinding.inflate(mInflater,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position], mContext)
        holder.itemView.setOnClickListener {
            onClickItems?.invoke(mData[position])
            ZoneActivity.appPreferences = AppPreferences(mContext)
            var parDatetime = ""
            var parImage = ""
            var parLatlng = ""
            var parTimezone = ""
            var parUtcDatetime = ""
            var parUtcOffset = ""

            parDatetime= mData[position].datetime
            parImage = mData[position].image
            parLatlng = mData[position].latlng
            parTimezone = mData[position].timezone
            parUtcDatetime = mData[position].utc_datetime
            parUtcOffset = mData[position].utc_offset

            Log.i("TimeZoneAdapter", "parDatetime : $parDatetime")
            Log.i("TimeZoneAdapter", "parImage : $parImage")
            Log.i("TimeZoneAdapter", "parLatlng : $parLatlng")
            Log.i("TimeZoneAdapter", "parTimezone : $parTimezone")
            Log.i("TimeZoneAdapter", "parUtcDatetime : $parUtcDatetime")
            Log.i("TimeZoneAdapter", "parUtcOffset : $parUtcOffset")

            ZoneActivity.appPreferences.setDatetime(parDatetime)
            ZoneActivity.appPreferences.setImage(parImage)
            ZoneActivity.appPreferences.setLatlang(parLatlng)
            ZoneActivity.appPreferences.setTimezone(parTimezone)
            ZoneActivity.appPreferences.setUtcDatetime(parUtcDatetime)
            ZoneActivity.appPreferences.setUtcOffset(parUtcOffset)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(private val binding: ItemListZonaBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Timezone, context: Context) {

            binding.apply {
                tvTimezone.text = item.timezone
//                tvKota.text = item.city
                tvDatetime.text = item.datetime
            }

            Glide.with(context)
                .load(R.drawable.img_bandung)
                .into(binding.imgZona)

//            Picasso.get()
//                .load(item.image)
//                .into(binding.imgZona)
        }
    }
}