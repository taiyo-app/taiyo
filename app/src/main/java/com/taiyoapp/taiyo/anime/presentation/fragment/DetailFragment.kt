package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
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
        viewModel.loadPoster(animeId)
        viewModel.loadAnimeDetail(animeId)
    }

    private fun observeViewModel() {
        viewModel.poster.observe(viewLifecycleOwner) {
            setPoster(it)
        }
        viewModel.animeDetail.observe(viewLifecycleOwner) {
            setAnimeDetail(it)
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

    private fun setPoster(poster: String) {
        Glide.with(this@DetailFragment)
            .load(poster)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.poster)
        Glide.with(this@DetailFragment)
            .load(poster)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.posterBg)
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
            // init timer
            if (animeDetail.nextEpisodeAt != 2000L) {
                timer.timeContainer.visibility = View.VISIBLE
                timer.nextSeriesNumber.text = requireContext().getString(
                    R.string.next_series_number,
                    animeDetail.episodesAired
                )
                viewModel.startTimer(animeDetail.nextEpisodeAt)
            } else {
                timer.timeContainer.visibility = View.GONE
            }
            with(info) {
                score.text = animeDetail.score
                scoreContent.text = requireContext().getString(R.string.max_rating)
                status.text = animeDetail.status
                episodes.text = requireContext().getString(
                    R.string.episodes,
                    animeDetail.episodesAired,
                    animeDetail.episodesTotal
                )
                kind.text = animeDetail.kind
                duration.text = animeDetail.duration
                studio.text = animeDetail.studio
                airedOn.text = viewModel.formatAiredOn(animeDetail.airedOn)
                val formattedDescription = animeDetail.description
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