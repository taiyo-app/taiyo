package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.Episodes.Translation
import com.taiyoapp.taiyo.anime.presentation.adapter.EpisodesAdapter
import com.taiyoapp.taiyo.anime.presentation.viewmodel.EpisodesViewModel
import com.taiyoapp.taiyo.databinding.FragmentWatchBinding

class WatchFragment : Fragment() {
    private var animeId: Int = UNDEFINED_ID
    private var title: String = UNDEFINED_TITLE
    private var kind: String = UNDEFINED_KIND

    private lateinit var viewModel: EpisodesViewModel

    private var _binding: FragmentWatchBinding? = null
    private val binding: FragmentWatchBinding
        get() = _binding ?: throw RuntimeException("FragmentWatchBinding is null")

    private var translations: List<Translation> = arrayListOf()

    private lateinit var adapter: EpisodesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[EpisodesViewModel::class.java]
        setupAppBar()
        setupImageButtons()
        setupEpisodesRecyclerView()
        observeViewModel()
        viewModel.loadTranslations(animeId)
    }

    override fun onResume() {
        super.onResume()
        // status bar color
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(), R.color.ui_bg
        )
    }

    private fun observeViewModel() {
        // translations
        viewModel.translations.observe(viewLifecycleOwner) {
            translations = it
            setupTranslationsAdapter()
        }
        // episodes
        viewModel.episodes.observe(viewLifecycleOwner) { episodes ->
            if (episodes.last().episodeNumber in 0..1) {
                binding.llSort.visibility = View.GONE
                adapter.submitList(episodes)
            } else {
                binding.llSort.visibility = View.VISIBLE
                if (binding.tbSort.isChecked) {
                    adapter.submitList(
                        episodes.sortedByDescending { it.episodeNumber }
                    )
                } else {
                    adapter.submitList(
                        episodes.sortedBy { it.episodeNumber }
                    )
                }
            }
        }
    }

    private fun setupAppBar() {
        binding.ibBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.tvAnimeTitle.text = title
    }

    private fun setupTranslationsAdapter() {
        binding.translations.setDropDownBackgroundDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.background_translations,
                null
            )
        )
        val adapterTranslations = ArrayAdapter(
            requireContext(),
            R.layout.item_translation,
            translations
        )
        binding.translations.setAdapter(adapterTranslations)
        if (translations[0].episodesCount == 0) {
            binding.translations.setText(requireContext().getString(
                R.string.item_translation_one_episode,
                translations[0].title
            ), false)
        } else {
            binding.translations.setText(requireContext().getString(
                R.string.item_translation,
                translations[0].title,
                translations[0].episodesCount.toString()
            ), false)
        }
        viewModel.loadEpisodes(translations[0].id)
        binding.translations.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val translation = parent?.adapter?.getItem(position) as Translation
                viewModel.loadEpisodes(translation.id)
            }
    }

    private fun setupImageButtons() {
        with (binding) {
            toggleButtonGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
                if (isChecked) {
                    var spanCount = 0
                    when (checkedId) {
                        R.id.b_grid_1 -> {
                            spanCount = 1
                        }
                        R.id.b_grid_2 -> {
                            spanCount = 2
                        }
                        R.id.b_grid_4 -> {
                            spanCount = 4
                        }
                    }
                    adapter.spanCount = spanCount
                    rvEpisodes.layoutManager = GridLayoutManager(context, spanCount)
                } else {
                    if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                        Toast.makeText(
                            context, "No Alignment Selected", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            tbSort.setOnClickListener {
                if (tbSort.isChecked) {
                    adapter.submitList(
                        adapter.currentList.sortedByDescending { it.episodeNumber }
                    )
                } else {
                    adapter.submitList(
                        adapter.currentList.sortedBy { it.episodeNumber }
                    )
                }
            }
        }
    }

    private fun setupEpisodesRecyclerView() {
        adapter = EpisodesAdapter(requireContext(), kind)
        with (binding) {
            rvEpisodes.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (args.containsKey(ANIME_ID) && args.containsKey(ANIME_TITLE)) {
            animeId = args.getInt(ANIME_ID)
            title = args.getString(ANIME_TITLE) ?: UNDEFINED_TITLE
            kind = args.getString(ANIME_KIND) ?: UNDEFINED_KIND
        } else {
            throw RuntimeException("Args doesn't contain a key")
        }
    }

    companion object {
        private const val ANIME_ID = "id"
        private const val ANIME_TITLE = "title"
        private const val ANIME_KIND = "kind"
        private const val UNDEFINED_ID = -1
        private const val UNDEFINED_TITLE = "UNDEFINED TITLE"
        private const val UNDEFINED_KIND = "UNDEFINED KIND"

        fun newInstance(id: Int, animeTitle: String, kind: String): WatchFragment {
            return WatchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID, id)
                    putString(ANIME_KIND, kind)
                    putString(ANIME_TITLE, animeTitle)
                }
            }
        }
    }
}