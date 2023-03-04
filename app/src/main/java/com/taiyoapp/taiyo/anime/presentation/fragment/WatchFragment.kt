package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.Episodes.Translation
import com.taiyoapp.taiyo.anime.presentation.adapter.EpisodesAdapter
import com.taiyoapp.taiyo.anime.presentation.util.EpisodesItemDecoration
import com.taiyoapp.taiyo.anime.presentation.viewmodel.EpisodeListViewModel
import com.taiyoapp.taiyo.databinding.FragmentWatchBinding

class WatchFragment : Fragment() {
    private var animeId: Int = UNDEFINED_ID
    private var animeTitle: String = UNDEFINED_TITLE

    private lateinit var viewModel: EpisodeListViewModel

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
        viewModel = ViewModelProvider(this)[EpisodeListViewModel::class.java]
        setupAppBar()
        setupImageButtons()
        setupEpisodesRecyclerView()
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

    private fun setupAppBar() {
        binding.tvAnimeTitle.text = animeTitle
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

    private fun setupImageButtons() {
        with (binding) {
             fun showToast(str: String) {
                Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT).show()
            }
            toggleButtonGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.b_grid_1 -> showToast("Left Align")
                        R.id.b_grid_2 -> showToast("Center Align")
                        R.id.b_grid_4 -> showToast("Right Align")
                    }
                } else {
                    if (toggleButtonGroup.checkedButtonId == View.NO_ID) {
                        showToast("No Alignment Selected")
                    }
                }
            }
            ibSort.setOnClickListener {
//                ibSort.setImageResource(requireContext().getDrawable(R.drawable.ic_up_24))
            }
        }
    }

    private fun setupEpisodesRecyclerView() {
        adapter = EpisodesAdapter(requireContext())
        with (binding) {
            rvEpisodes.adapter = adapter
            if (rvEpisodes.itemDecorationCount == 0) {
                rvEpisodes.addItemDecoration(
                    EpisodesItemDecoration(42, 0)
                )
            }
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
            animeTitle = args.getString(ANIME_TITLE) ?: UNDEFINED_TITLE
        } else {
            throw RuntimeException("Args doesn't contain a key")
        }
    }

    companion object {
        private const val ANIME_ID = "id"
        private const val ANIME_TITLE = "anime_title"
        private const val UNDEFINED_ID = -1
        private const val UNDEFINED_TITLE = "UNDEFINED TITLE"

        fun newInstance(id: Int, animeTitle: String): WatchFragment {
            return WatchFragment().apply {
                arguments = Bundle().apply {
                    putInt(ANIME_ID, id)
                    putString(ANIME_TITLE, animeTitle)
                }
            }
        }
    }
}