package com.example.holidaysoverlapp.ui.country

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.holidaysoverlapp.data.entities.Country

class CountryListAdapter(private val pickCountry: (code: String) -> Unit) :
    ListAdapter<Country, CountryListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryListViewHolder {
        return CountryListViewHolder.create(parent, pickCountry)
    }

    override fun onBindViewHolder(holder: CountryListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Country>() {
            override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
                return oldItem.code == newItem.code
            }

            override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
                return oldItem == newItem
            }
        }
    }

}