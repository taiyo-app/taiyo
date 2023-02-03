package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.MediaQuery
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.presentation.util.indentedText
import com.taiyoapp.taiyo.anime.presentation.viewmodel.AnimeDetailViewModel
import com.taiyoapp.taiyo.databinding.FragmentAnimeDetailBinding

class AnimeDetailFragment : Fragment() {
    private var animeId: Int = UNDEFINED_ID

    private lateinit var viewModel: AnimeDetailViewModel

    private var _binding: FragmentAnimeDetailBinding? = null
    private val binding: FragmentAnimeDetailBinding
        get() = _binding ?: throw RuntimeException("FragmentAnimeDetailBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
        initViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.bg)
        _binding = null
    }

    private fun setupFragment() {
        // StatusBar
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(),
            android.R.color.transparent
        )
        // AppBar
        with(binding) {
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset: Int ->
                if (verticalOffset - 250 > -appBarLayout.totalScrollRange) {
                    fabBack.backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(), R.color.bg
                    )
                    fabBack.show()
                } else if (verticalOffset - 56 > -appBarLayout.totalScrollRange) {
                    fabBack.hide()
                } else {
                    fabBack.backgroundTintList = ContextCompat.getColorStateList(
                        requireContext(), R.color.ui_bg
                    )
                    fabBack.show()
                }
            }
        }
        // NavBar
//        binding.detailBottomBar.setOnItemSelectedListener {
//            val fragment = when (it.itemId) {
//                R.id.tab_info ->
//                else -> throw RuntimeException("Wrong newTab id in AnimeDetailFragment")
//            }
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerContent, fragment)
//                .commit()
//            return@setOnItemSelectedListener true
//        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AnimeDetailViewModel::class.java]
        observeViewModel()
        viewModel.loadAnimeDetail(animeId)
        viewModel.loadAnimeMedia(animeId)
    }

    private fun observeViewModel() {
        viewModel.animeDetail.observe(viewLifecycleOwner) {
            if (it != null) {
                setAnimeDetail(it)
            }
        }
        viewModel.animeMedia.observe(viewLifecycleOwner) {
            if (it != null) {
                setAnimeMedia(it)
            }
        }
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            val pair = it
            val keys = pair.first
            val values = pair.second
            with(binding) {
                with(timer) {
                    daysLabel.text = keys[0]; days.text = values[0].toString()
                    hoursLabel.text = keys[1]; hours.text = values[1].toString()
                    minutesLabel.text = keys[2]; minutes.text = values[2].toString()
                    secondsLabel.text = keys[3]; seconds.text = values[3].toString()
                }
            }
        }
    }

    private fun setAnimeDetail(animeDetail: AnimeDetail) {
        // setup main info
        with(binding) {
            toolBar.title = animeDetail.russian
            status.text = viewModel.parseStatus(animeDetail.status)
            with(info) {
                episodes.text = requireContext().getString(
                    R.string.episodes,
                    animeDetail.episodesAired,
                    animeDetail.episodesTotal
                )
                kind.text = viewModel.formatKind(animeDetail.kind)
                duration.text = viewModel.formatDuration(animeDetail.duration)
                if (animeDetail.description != null) {
                    val formattedDescription = viewModel.formatDescription(animeDetail.description)
                    description.text = indentedText(
                        formattedDescription,
                        72,
                        0
                    )
                    description.setOnClickListener {
                        if (description.maxLines == 5) {
                            description.maxLines = Int.MAX_VALUE
                        } else {
                            description.maxLines = 5
                        }
                    }
                } else {
                    description.text = indentedText(
                        requireContext().getString(R.string.empty_description),
                        72,
                        0
                    )
                }
                studio.text = viewModel.formatStudio(animeDetail.studios)
            }
        }
    }

    private fun setAnimeMedia(media: MediaQuery.Data) {
        with(binding) {
            // load image
            com.bumptech.glide.Glide.with(this@AnimeDetailFragment)
                .load(media.Media?.coverImage?.extraLarge)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)
            // init timer
            if (media.Media?.airingSchedule?.nodes?.isEmpty() == false) {
                val node = media.Media.airingSchedule.nodes[0]
                timer.timeContainer.visibility = View.VISIBLE
                timer.nextSeriesNumber.text = requireContext().getString(
                    R.string.next_series_number,
                    node?.episode?.toString()
                )
                node?.timeUntilAiring?.toLong()?.let { viewModel.startTimer(it) }
            } else {
                timer.timeContainer.visibility = View.GONE
            }
            with(info) {
                rating.text = viewModel.formatRating(media.Media?.meanScore.toString())
                ratingContent.text = requireContext().getString(R.string.max_rating)
                season.text = viewModel.formatSeason(
                    media.Media?.season?.name,
                    media.Media?.seasonYear
                )
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

        fun newInstance(id: Int): AnimeDetailFragment {
            return AnimeDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID, id)
                }
            }
        }
    }
}