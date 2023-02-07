package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.EpisodeList.Translation
import com.taiyoapp.taiyo.anime.presentation.adapter.EpisodeListAdapter
import com.taiyoapp.taiyo.anime.presentation.viewmodel.EpisodeListViewModel
import com.taiyoapp.taiyo.databinding.FragmentWatchBinding

class WatchFragment : Fragment() {
    private var animeId: Int = UNDEFINED_ID

    private lateinit var viewModel: EpisodeListViewModel

    private var _binding: FragmentWatchBinding? = null
    private val binding: FragmentWatchBinding
        get() = _binding ?: throw RuntimeException("FragmentWatchBinding is null")

    private var translations: List<Translation> = arrayListOf()

    private lateinit var adapter: EpisodeListAdapter

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
        setupEpisodesRecyclerView()
        viewModel = ViewModelProvider(this)[EpisodeListViewModel::class.java]
        observeViewModel()
        viewModel.loadTranslations(animeId)
    }

    private fun observeViewModel() {
        // translations
        viewModel.translations.observe(viewLifecycleOwner) {
            translations = it
            setupTranslationsAdapter()
        }
        // episodes
        viewModel.episodes.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupTranslationsAdapter() {
        val adapterTranslations = ArrayAdapter(
            requireContext(),
            R.layout.item_translation,
            translations
        )
        binding.translations.setAdapter(adapterTranslations)
        binding.translations.setText(translations[0].title, false)
        viewModel.loadEpisodes(translations[0].id)
        binding.translations.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val translation = parent?.adapter?.getItem(position) as Translation
                adapter.episodeCounter = 1
                viewModel.loadEpisodes(translation.id)
            }
    }

    private fun setupEpisodesRecyclerView() {
        adapter = EpisodeListAdapter(requireContext())
        binding.rvEpisodes.adapter = adapter
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

        fun newInstance(id: Int): WatchFragment {
            return WatchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID, id)
                }
            }
        }
    }
}