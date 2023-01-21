package com.taiyoapp.taiyo.anime.presentation.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.presentation.util.simpleScan
import com.taiyoapp.taiyo.anime.presentation.adapter.MainLoadStateAdapter
import com.taiyoapp.taiyo.anime.presentation.adapter.OngoingsAdapter
import com.taiyoapp.taiyo.anime.presentation.adapter.RefreshAction
import com.taiyoapp.taiyo.anime.presentation.viewmodel.OngoingsViewModel
import com.taiyoapp.taiyo.databinding.FragmentAnimeListBinding
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OngoingsFragment : Fragment() {
    private lateinit var viewModel: OngoingsViewModel

    private var _binding: FragmentAnimeListBinding? = null
    private val binding: FragmentAnimeListBinding
        get() = _binding ?: throw RuntimeException("FragmentAnimeListBinding is null")

    private lateinit var adapter: OngoingsAdapter

    private lateinit var mainLoadStateViewHolder: MainLoadStateAdapter.MainLoaderViewHolder

    private lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnimeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[OngoingsViewModel::class.java]
        setupAnimeList()
        setupSwipeToRefresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAnimeList() {
        setupRecyclerView()

        observeAnimeList(adapter)
        observeLoadState(adapter)

        handleScrollingToTopWhenLoaded(adapter)
        handleListVisibility(adapter)
    }

    private fun setupRecyclerView() {
        adapter = OngoingsAdapter(requireContext())
        val refreshAction: RefreshAction = {
            adapter.retry()
        }
        val footerAdapter = MainLoadStateAdapter(refreshAction)
        val adapterWithLoadState = adapter.withLoadStateFooter(footerAdapter)
        val spanCount = if (
            this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        ) 3 else 6
        layoutManager = GridLayoutManager(requireContext(), spanCount)
        with(binding) {
            rvAnime.adapter = adapterWithLoadState
            rvAnime.layoutManager = layoutManager
        }
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
                    spanCount
                } else {
                    1
                }
            }
        }
        mainLoadStateViewHolder = MainLoadStateAdapter.MainLoaderViewHolder(
            binding.loadStateView,
            binding.swipeRefreshLayout,
            refreshAction
        )
    }

    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.loading, R.color.loading
        )
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.bg)
    }

    private fun observeAnimeList(adapter: OngoingsAdapter) {
        lifecycleScope.launch {
            viewModel.animeFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeLoadState(adapter: OngoingsAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(500).collectLatest { state ->
                mainLoadStateViewHolder.bind(state.refresh)
            }
        }
    }

    private fun handleScrollingToTopWhenLoaded(adapter: OngoingsAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 2)
            .collectLatest { (previousState, currentState) ->
                if (previousState is LoadState.Loading && currentState is LoadState.NotLoading) {
                    if (_binding != null) {
                        binding.rvAnime.scrollToPosition(0)
                    }
                }
            }
    }

    private fun handleListVisibility(adapter: OngoingsAdapter) = lifecycleScope.launch {
        getRefreshLoadStateFlow(adapter)
            .simpleScan(count = 3)
            .collectLatest { (beforePrevious, previous, current) ->
                if (_binding != null) {
                    binding.rvAnime.isInvisible = current is LoadState.Error
                            || previous is LoadState.Error
                            || (beforePrevious is LoadState.Error && previous is LoadState.NotLoading
                            && current is LoadState.Loading)
                }
            }
    }

    private fun getRefreshLoadStateFlow(adapter: OngoingsAdapter): Flow<LoadState> {
        return adapter.loadStateFlow.map { it.refresh }
    }

    companion object {
        fun newInstance() = OngoingsFragment()
    }
}