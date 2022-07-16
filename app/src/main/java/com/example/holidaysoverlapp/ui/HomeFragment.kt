package com.example.holidaysoverlapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.holidaysoverlapp.R
import com.example.holidaysoverlapp.databinding.FragmentHomeBinding
import com.example.holidaysoverlapp.databinding.ViewSpecialStateBinding
import com.example.holidaysoverlapp.ui.country.CountryListAdapter
import com.example.holidaysoverlapp.ui.holiday.HolidayListAdapter
import com.example.holidaysoverlapp.ui.util.EntityListViewState.*
import com.example.holidaysoverlapp.ui.util.ErrorType
import com.example.holidaysoverlapp.ui.util.HolidaysFilterType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var countryListAdapter: CountryListAdapter
    private lateinit var holidayListAdapter: HolidayListAdapter
    private val viewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var _specialStateViewBinding: ViewSpecialStateBinding? = null
    private val specialStateViewBinding get() = _specialStateViewBinding!!

    private var menu: Menu? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)
        _specialStateViewBinding = ViewSpecialStateBinding.bind(binding.root)
        specialStateViewBinding.specialStateGroup.isInvisible = true

        setupActionBarMenu()
        setUpCountryRecyclerView()
        setUpHolidayRecyclerView()

        binding.swipeToRefresh.apply {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.refresh()
            }
        }

        specialStateViewBinding.refreshButton.setOnClickListener {
            viewModel.refresh()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.countryViewState.collect { countryViewState ->
                    when (countryViewState) {
                        is Success -> {
                            if (countryViewState.data.isEmpty()) {
                                handleEmptyState()
                            } else {
                                specialStateViewBinding.specialStateGroup.isInvisible = true
                                binding.swipeToRefresh.isEnabled = true
                            }
                            binding.loadingCircularProgressIndicator.visibility = View.GONE
                            countryListAdapter.submitList(countryViewState.data)
                        }
                        is Loading -> {
                            binding.loadingCircularProgressIndicator.visibility = View.VISIBLE
                        }
                        is Error -> {
                            if (countryViewState.errorType == ErrorType.NoInternet) {
                                handleNoInternetConnectionState()
                            } else {
                                binding.loadingCircularProgressIndicator.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    R.string.generic_error_message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.holidayViewState.collect { holidayViewState ->
                    when (holidayViewState) {
                        is Success -> {
                            specialStateViewBinding.specialStateGroup.isInvisible = true
                            binding.swipeToRefresh.isEnabled = true
                            binding.loadingCircularProgressIndicator.visibility = View.GONE
                            holidayListAdapter.submitList(holidayViewState.data)
                        }
                        is Loading -> {
                            binding.loadingCircularProgressIndicator.visibility = View.VISIBLE
                        }
                        is Error -> {
                            if (holidayViewState.errorType == ErrorType.NoInternet) {
                                handleNoInternetConnectionState()
                            } else {
                                binding.loadingCircularProgressIndicator.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    R.string.generic_error_message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupActionBarMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.filters, menu)
                this@HomeFragment.menu = menu
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                val filterType = when (menuItem.itemId) {
                    R.id.onlyA -> HolidaysFilterType.OnlyA
                    R.id.onlyB -> HolidaysFilterType.OnlyB
                    else -> HolidaysFilterType.Intersection
                }
                viewModel.changeFilterType(filterType)
                return true
            }
        })
    }

    private fun setUpCountryRecyclerView() {
        binding.countryList.apply {
            countryListAdapter = CountryListAdapter { countryCode ->
                viewModel.toggleCountrySelectedState(countryCode)
            }
            adapter = countryListAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL)
        }
    }

    private fun setUpHolidayRecyclerView() {
        binding.holidayList.apply {
            holidayListAdapter = HolidayListAdapter()
            adapter = holidayListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun handleNoInternetConnectionState() {
        specialStateViewBinding.specialStateMessageTextView.text =
            getString(R.string.internet_connection_issues_message)
        specialStateViewBinding.specialStateIconImageView.setImageDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.ic_baseline_wifi_off_24
            )
        )
        specialStateViewBinding.specialStateGroup.isVisible = true
        binding.swipeToRefresh.isEnabled = false
    }

    private fun handleEmptyState() {
        specialStateViewBinding.specialStateMessageTextView.text =
            getString(R.string.empty_list_message)
        specialStateViewBinding.specialStateIconImageView.setImageDrawable(
            ContextCompat.getDrawable(
                binding.root.context,
                R.drawable.ic_baseline_self_improvement_24
            )
        )
        specialStateViewBinding.specialStateGroup.isVisible = true
        binding.swipeToRefresh.isEnabled = false
    }

}