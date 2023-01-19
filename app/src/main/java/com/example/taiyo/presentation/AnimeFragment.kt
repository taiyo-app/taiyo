package com.example.taiyo.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.taiyo.R
import com.example.taiyo.databinding.FragmentAnimeBinding
import com.example.taiyo.presentation.anime.page.adapters.ViewPagerAdapter
import com.example.taiyo.presentation.anime.page.anons.AnonsFragment
import com.example.taiyo.presentation.anime.page.movies.MoviesFragment
import com.example.taiyo.presentation.anime.page.ongoings.OngoingsFragment
import com.example.taiyo.presentation.anime.page.released.ReleasedFragment
import com.google.android.material.tabs.TabLayoutMediator

class AnimeFragment : Fragment() {

    private var _binding: FragmentAnimeBinding? = null
    private val binding: FragmentAnimeBinding
        get() = _binding ?: throw RuntimeException("FragmentAnimeBinding is null")

    private val fragmentList = listOf(
        OngoingsFragment.newInstance(),
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
        val viewPagerAdapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager2.adapter = viewPagerAdapter
        val fragmentListTitles = listOf(
            getString(R.string.ongoings),
            getString(R.string.anons),
            getString(R.string.released),
            getString(R.string.movies)
        )
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = fragmentListTitles[position]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}