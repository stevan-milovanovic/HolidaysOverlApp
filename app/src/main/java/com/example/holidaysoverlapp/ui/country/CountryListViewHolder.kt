package com.example.holidaysoverlapp.ui.country

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.holidaysoverlapp.R
import com.example.holidaysoverlapp.data.entities.Country
import com.example.holidaysoverlapp.databinding.CountryListItemBinding

class CountryListViewHolder(private val binding: CountryListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var countryCode: String? = null

    fun bind(country: Country) {
        Glide.with(binding.root.context)
            .asBitmap()
            .load(country.flag)
            .placeholder(R.drawable.ic_baseline_circle_24)
            .circleCrop()
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    binding.countryChip.chipIcon = BitmapDrawable(itemView.resources, resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    binding.countryChip.chipIcon = placeholder
                }
            })
        binding.countryChip.text = country.name
        binding.countryChip.isChecked = country.selected
        countryCode = country.code
    }

    companion object {

        fun create(parent: ViewGroup, pickCountry: (code: String) -> Unit): CountryListViewHolder {
            val countryListItemBinding = CountryListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            val viewHolder = CountryListViewHolder(countryListItemBinding)
            viewHolder.binding.countryChip.setOnClickListener {
                viewHolder.countryCode?.let {
                    pickCountry.invoke(it)
                }
            }
            return viewHolder
        }

    }
}