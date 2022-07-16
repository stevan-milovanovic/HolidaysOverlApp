package com.example.holidaysoverlapp.ui.holiday

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.holidaysoverlapp.data.entities.Holiday
import com.example.holidaysoverlapp.databinding.HolidayListItemBinding

class HolidayListViewHolder(private val binding: HolidayListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(holiday: Holiday) {
        binding.holidayNameTextView.text = holiday.name
        binding.holidayDateTextView.text = holiday.date
    }

    companion object {
        fun create(parent: ViewGroup) = HolidayListViewHolder(
            HolidayListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}