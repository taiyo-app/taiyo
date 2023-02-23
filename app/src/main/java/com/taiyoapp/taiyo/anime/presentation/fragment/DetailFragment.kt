package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.taiyoapp.taiyo.MediaQuery
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.presentation.util.indentedText
import com.taiyoapp.taiyo.anime.presentation.viewmodel.DetailViewModel
import com.taiyoapp.taiyo.databinding.FragmentDetailBinding


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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        observeViewModel()
        viewModel.loadAnimeDetail(animeId)
        viewModel.loadAnimeMedia(animeId)
    }

    private fun observeViewModel() {
        viewModel.animeDetail.observe(viewLifecycleOwner) {
            setAnimeDetail(it)
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
        with(binding) {
            title.text = animeDetail.russian
            titleEng.text = animeDetail.name
            bWatch.setOnClickListener {
                val watchFragment = WatchFragment.newInstance(animeId)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, watchFragment)
                    .addToBackStack(null)
                    .commit()
            }
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
//            status.text = viewModel.parseStatus(animeDetail.status)
    }

    private fun setAnimeMedia(media: MediaQuery.Data) {
        with(binding) {
            // load image
            Glide.with(this@DetailFragment)
                .load(media.Media?.coverImage?.extraLarge)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)
            Glide.with(this@DetailFragment)
                .load(media.Media?.coverImage?.extraLarge)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(posterBg)
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

        fun newInstance(id: Int): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID, id)
                }
            }
        }
    }
}