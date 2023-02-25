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
            if (animeDetail.score != "~") {
                llScore.visibility = View.VISIBLE
                rbScore.rating = animeDetail.score.toFloat() / 2
                score.text = animeDetail.score
            } else {
                llScore.visibility = View.GONE
            }
            bWatch.setOnClickListener {
                val watchFragment = WatchFragment.newInstance(animeId)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, watchFragment)
                    .addToBackStack(null)
                    .commit()
            }
            // init timer
            if (animeDetail.nextEpisodeAt != -1L) {
                timer.timeContainer.visibility = View.VISIBLE
                val nextSeriesNumber = animeDetail.episodesAired.toInt() + 1
                timer.nextSeriesNumber.text = requireContext().getString(
                    R.string.next_series_number,
                    nextSeriesNumber.toString()
                )
                binding.timer.timerProgramCountdown.startCountDown(animeDetail.nextEpisodeAt)
            } else {
                timer.timeContainer.visibility = View.GONE
            }
            with(info) {
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