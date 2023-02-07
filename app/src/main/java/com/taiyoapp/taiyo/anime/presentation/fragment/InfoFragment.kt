package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.taiyoapp.taiyo.MediaQuery
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.AnimeDetail
import com.taiyoapp.taiyo.anime.presentation.util.indentedText
import com.taiyoapp.taiyo.anime.presentation.viewmodel.DetailViewModel
import com.taiyoapp.taiyo.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private var animeId: Int = UNDEFINED_ID

    private val viewModel: DetailViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: FragmentInfoBinding? = null
    private val binding: FragmentInfoBinding
        get() = _binding ?: throw RuntimeException("FragmentInfoBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
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
    }

    private fun setAnimeDetail(animeDetail: AnimeDetail) {
        // setup main info
        with(binding) {
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

    private fun setAnimeMedia(media: MediaQuery.Data) {
        with(binding) {
            rating.text = viewModel.formatRating(media.Media?.meanScore.toString())
            ratingContent.text = requireContext().getString(R.string.max_rating)
            season.text = viewModel.formatSeason(
                media.Media?.season?.name,
                media.Media?.seasonYear
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        fun newInstance(id: Int): InfoFragment {
            return InfoFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID, id)
                }
            }
        }
    }
}