package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.db.williamchart.data.Scale
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.taiyoapp.taiyo.MainActivity
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.DetailMal
import com.taiyoapp.taiyo.anime.domain.entity.DetailShiki
import com.taiyoapp.taiyo.anime.presentation.adapter.InfoSection
import com.taiyoapp.taiyo.anime.presentation.adapter.ScreenshotsAdapter
import com.taiyoapp.taiyo.anime.presentation.adapter.SimilarAdapter
import com.taiyoapp.taiyo.anime.presentation.adapter.VideoKindAdapter
import com.taiyoapp.taiyo.anime.presentation.util.HorizontalItemDecoration
import com.taiyoapp.taiyo.anime.presentation.util.formatVideoKind
import com.taiyoapp.taiyo.anime.presentation.viewmodel.DetailViewModel
import com.taiyoapp.taiyo.databinding.FragmentDetailBinding
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter


class DetailFragment : Fragment() {
    private var animeId: Int = UNDEFINED_ID

    private lateinit var viewModel: DetailViewModel

    private var _binding: FragmentDetailBinding? = null
    private val binding: FragmentDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentDetailBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        // status bar color
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(), R.color.bg
        )
        // show bottom navigation view
        val bottomNavigationView = (context as MainActivity).findViewById<BottomNavigationView>(
            R.id.main_bottom_bar
        )
        bottomNavigationView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        observeViewModel()
        viewModel.loadDetailMal(animeId)
        viewModel.loadDetailShiki(animeId)
    }

    private fun observeViewModel() {
        viewModel.detailShiki.observe(viewLifecycleOwner) {
            setDetailShiki(it)
        }
        viewModel.detailMAL.observe(viewLifecycleOwner) {
            setDetailMAL(it)
        }
        viewModel.video.observe(viewLifecycleOwner) { videoList ->
            if (videoList.isNotEmpty()) {
                val videoKindAdapter = VideoKindAdapter(requireContext())
                with(videoKindAdapter) {
                    submitList(viewModel.getVideoKinds(videoList))
                    onVideoItemClick = {
                        val videoByKind = viewModel.getVideoByKind(it.kind)
                        val formattedVideoKind = formatVideoKind(videoByKind[0].kind)
                        val fragment = VideoFragment.newInstance(formattedVideoKind, videoByKind)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
                with(binding.layoutVideoKinds) {
                    rrVideos.adapter = videoKindAdapter
                    if (rrVideos.itemDecorationCount == 0) {
                        rrVideos.addItemDecoration(
                            HorizontalItemDecoration(16, 32)
                        )
                    }
                    llVideoKinds.visibility = View.VISIBLE
                }
            } else {
                binding.layoutVideoKinds.llVideoKinds.visibility = View.GONE
            }
        }
        viewModel.screenshots.observe(viewLifecycleOwner) { screenshots ->
            if (screenshots.isNotEmpty()) {
                val screenshotsAdapter = ScreenshotsAdapter(requireContext())
                with(screenshotsAdapter) {
                    submitList(screenshots)
                    onScreenshotClick = {
                        val fragment = ScreenshotsFragment.newInstance(screenshots, it)
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container_view, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
                with(binding.layoutScreenshots) {
                    rrScreenshots.adapter = screenshotsAdapter
                    if (rrScreenshots.itemDecorationCount == 0) {
                        rrScreenshots.addItemDecoration(
                            HorizontalItemDecoration(16, 32)
                        )
                    }
                    llScreenshots.visibility = View.VISIBLE
                }
            } else {
                binding.layoutScreenshots.llScreenshots.visibility = View.GONE
            }
        }
        viewModel.similar.observe(viewLifecycleOwner) { similar ->
            if (similar.isNotEmpty()) {
                val similarAdapter = SimilarAdapter(requireContext())
                with(similarAdapter) {
                    submitList(similar)
                    onAnimeClick = {
                        Toast.makeText(requireContext(), "OnClick", Toast.LENGTH_SHORT).show()
                    }
                }
                with(binding.layoutSimilar) {
                    rrSimilar.adapter = similarAdapter
                    if (rrSimilar.itemDecorationCount == 0) {
                        rrSimilar.addItemDecoration(
                            HorizontalItemDecoration(18, 36)
                        )
                    }
                    llSimilar.visibility = View.VISIBLE
                }
            } else {
                binding.layoutSimilar.llSimilar.visibility = View.GONE
            }
        }
    }

    private fun setDetailShiki(detailShiki: DetailShiki) {
        with(binding) {
            // score
            if (detailShiki.status != "Анонс") {
                val score = detailShiki.score
                layoutRating.tvRating.text = detailShiki.score
                llScore.visibility = View.VISIBLE
                rbScore.rating = score.toFloat() / 2
                tvScore.text = detailShiki.score
            } else {
                llScore.visibility = View.GONE
            }

            // title
            title.text = detailShiki.russian
            titleEng.text = detailShiki.name

            // info
            val sectionedAdapter = SectionedRecyclerViewAdapter()
            val infoMap = viewModel.getInfoMapFromDetail(detailShiki)
            for (info in infoMap) {
                sectionedAdapter.addSection(
                    InfoSection(info.key, info.value, requireContext())
                )
            }
            with(layoutInfo) {
                rrInfo.adapter = sectionedAdapter
                if (rrInfo.itemDecorationCount == 0) {
                    rrInfo.addItemDecoration(
                        HorizontalItemDecoration(16, 40)
                    )
                }
                layoutInfo.flInfo.visibility = View.VISIBLE
            }

            // fab watch
            fabWatch.setOnClickListener {
                val watchFragment = WatchFragment.newInstance(animeId, detailShiki.russian)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, watchFragment)
                    .addToBackStack(null)
                    .commit()
            }

            // init timer
            if (detailShiki.nextEpisodeAt != -1L) {
                with(binding.layoutTimer) {
                    clTimer.visibility = View.VISIBLE
                    val nextSeriesNumberValue = detailShiki.episodesAired.toInt() + 1
                    nextSeriesNumber.text = requireContext().getString(
                        R.string.next_series_number,
                        nextSeriesNumberValue.toString()
                    )
                    timerProgramCountdown.startCountDown(detailShiki.nextEpisodeAt)
                }
            } else {
                binding.layoutTimer.clTimer.visibility = View.GONE
            }

            // description
            if (detailShiki.description != "Описание отсутствует") {
                with(binding.layoutDescription) {
                    llDescription.visibility = View.VISIBLE
                    tvDescription.text = detailShiki.description
                    fun descriptionOnClickListener() {
                        if (tvDescription.maxLines == 5) {
                            tvDescription.maxLines = Int.MAX_VALUE
                            tvShowHideDescription.text =
                                requireContext().getString(R.string.hideDetail)
                        } else {
                            tvDescription.maxLines = 5
                            tvShowHideDescription.text = requireContext().getString(R.string.detail)
                        }
                    }
                    tvDescription.setOnClickListener { descriptionOnClickListener() }
                    tvShowHideDescription.setOnClickListener { descriptionOnClickListener() }
                }
            } else {
                binding.layoutDescription.llDescription.visibility = View.GONE
            }

            // rating
            with(layoutRating) {
                if (detailShiki.status != "Анонс" && detailShiki.ratesScoresStats.isNotEmpty()) {
                    llRating.visibility = View.VISIBLE
                    layoutRating.horizontalBar.scale = Scale(0F, 125F)
                    layoutRating.horizontalBar.animate(detailShiki.ratesScoresStats)
                } else {
                    layoutRating.llRating.visibility = View.GONE
                }
            }
        }
    }

    private fun setDetailMAL(detailMAL: DetailMal) {
        with(binding) {
            // poster & background
            Glide.with(this@DetailFragment)
                .load(detailMAL.mainPicture.large)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)
            Glide.with(this@DetailFragment)
                .load(detailMAL.mainPicture.large)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(posterBg)

            // rating
            layoutRating.tvNumScoringUsers.text = requireContext().getString(
                R.string.ratesScoresStats,
                detailMAL.numScoringUsers.toString()
            )
            // status
            with(layoutStatus) {
                if (detailMAL.statistics.status.planToWatch != "") {
                    llStatus.visibility = View.VISIBLE
                    tvWatching.text = detailMAL.statistics.status.watching
                    tvCompleted.text = detailMAL.statistics.status.completed
                    tvPlanToWatch.text = detailMAL.statistics.status.planToWatch
                    tvOnHold.text = detailMAL.statistics.status.onHold
                    tvDropped.text = detailMAL.statistics.status.dropped
                    val amounts = viewModel.getStatusStatsForCharts(detailMAL.statistics.status)

                    val barEntry = ArrayList<BarEntry>()
                    barEntry.add(BarEntry(0f,
                        floatArrayOf(amounts[0], amounts[1], amounts[2], amounts[3], amounts[4])))
                    val colors: IntArray = intArrayOf(
                        requireContext().getColor(R.color.watching),
                        requireContext().getColor(R.color.planToWatch),
                        requireContext().getColor(R.color.completed),
                        requireContext().getColor(R.color.onHold),
                        requireContext().getColor(R.color.dropped)
                    )

                    val barDataSet = BarDataSet(barEntry, "Bar Set")
                    barDataSet.colors = colors.toList()
                    barDataSet.setDrawIcons(false)
                    barDataSet.setDrawValues(false)

                    val barData = BarData(barDataSet)
                    barData.barWidth = 1F

                    horizontalBarChart.description = null
                    horizontalBarChart.axisLeft.isEnabled = false
                    horizontalBarChart.axisRight.isEnabled = false
                    horizontalBarChart.xAxis.isEnabled = false
                    horizontalBarChart.legend.isEnabled = false
                    horizontalBarChart.setScaleEnabled(false)
                    horizontalBarChart.setDrawBarShadow(false)
                    horizontalBarChart.setDrawGridBackground(false)
                    horizontalBarChart.axisLeft.axisMinimum = 0F
                    horizontalBarChart.axisLeft.axisMaximum = amounts.last().toFloat()
                    horizontalBarChart.setViewPortOffsets(0F, 0F, 0F, 0F)
                    horizontalBarChart.moveViewTo(0f, 0f, YAxis.AxisDependency.LEFT)
                    horizontalBarChart.invalidate()

                    horizontalBarChart.data = barData
                } else {
                    binding.layoutRating.llRating.visibility = View.GONE
                }
            }
        }
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (args.containsKey(ANIME_ID)) {
            animeId = args.getInt(ANIME_ID)
        } else {
            throw RuntimeException("Args doesn't contain a key")
        }
    }

    companion object {
        private const val ANIME_ID = "id"
        private const val UNDEFINED_ID = -1

        fun newInstance(id: Int): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID, id)
                }
            }
        }
    }
}