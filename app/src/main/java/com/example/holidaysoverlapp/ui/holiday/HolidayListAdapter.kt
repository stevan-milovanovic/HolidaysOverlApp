package com.example.holidaysoverlapp.ui.holiday

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.holidaysoverlapp.data.entities.Holiday

class HolidayListAdapter : ListAdapter<Holiday, HolidayListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolidayListViewHolder {
        return HolidayListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: HolidayListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Holiday>() {
            override fun areItemsTheSame(oldItem: Holiday, newItem: Holiday): Boolean {
                return oldItem.uuid == newItem.uuid
            }

            override fun areContentsTheSame(oldItem: Holiday, newItem: Holiday): Boolean {
                return oldItem == newItem
            }
        }
    }

}