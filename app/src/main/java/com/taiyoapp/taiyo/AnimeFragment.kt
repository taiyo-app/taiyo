package com.taiyoapp.taiyo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import com.taiyoapp.taiyo.anime.presentation.adapter.ViewPagerAdapter
import com.taiyoapp.taiyo.anime.presentation.fragment.*
import com.taiyoapp.taiyo.databinding.FragmentAnimeBinding

open class AnimeFragment : Fragment() {
    private var _binding: FragmentAnimeBinding? = null
    private val binding: FragmentAnimeBinding
        get() = _binding ?: throw RuntimeException("FragmentAnimeBinding is null")

    private val fragmentList = listOf(OngoingsFragment.newInstance(),
        AnonsFragment.newInstance(),
        ReleasedFragment.newInstance(),
        MoviesFragment.newInstance()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        val viewPagerAdapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager2.adapter = viewPagerAdapter
        val fragmentListTitles = listOf(getString(R.string.ongoings),
            getString(R.string.anons),
            getString(R.string.released),
            getString(R.string.movies))
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = fragmentListTitles[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView = (context as MainActivity).findViewById<BottomNavigationView>(
            R.id.main_bottom_bar
        )
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAppBar() {
        with(binding.search) {
            ibSearch.setOnClickListener {
                launchSearchFragment()
            }
            tvSearch.setOnClickListener {
                launchSearchFragment()
            }
        }
    }

    private fun launchSearchFragment() {
        val searchFragment = SearchFragment.newInstance()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, searchFragment).addToBackStack(null).commit()
    }
}