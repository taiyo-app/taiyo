package com.taiyoapp.taiyo.anime.presentation.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.taiyoapp.taiyo.anime.presentation.util.simpleScan
import com.example.taiyo.databinding.FragmentSearchBinding
import com.taiyoapp.taiyo.anime.presentation.adapter.MainLoadStateAdapter
import com.taiyoapp.taiyo.anime.presentation.adapter.RefreshAction
import com.taiyoapp.taiyo.anime.presentation.adapter.SearchAdapter
import com.taiyoapp.taiyo.anime.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel

    private var _binding: FragmentSearchBinding? = null
    private val binding: FragmentSearchBinding
        get() = _binding ?: throw RuntimeException("FragmentAnimeListBinding is null")

    private lateinit var adapter: SearchAdapter

    private lateinit var mainLoadStateViewHolder: MainLoadStateAdapter.SearchLoaderViewHolder

    private lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        setupAppBar()
        setupSearchInput()
        setupAnimeList()
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
        adapter = SearchAdapter(requireContext())
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
            rvAnime.itemAnimator = null
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
        mainLoadStateViewHolder = MainLoadStateAdapter.SearchLoaderViewHolder(
            binding.loadStateView,
            refreshAction
        )
    }

    private fun observeAnimeList(adapter: SearchAdapter) {
        lifecycleScope.launch {
            viewModel.animeFlow.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeLoadState(adapter: SearchAdapter) {
        lifecycleScope.launch {
            adapter.loadStateFlow.debounce(500).collectLatest { state ->
                mainLoadStateViewHolder.bind(state.refresh)
            }
        }
    }

    private fun handleScrollingToTopWhenLoaded(adapter: SearchAdapter) = lifecycleScope.launch {
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

    private fun handleListVisibility(adapter: SearchAdapter) = lifecycleScope.launch {
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

    private fun getRefreshLoadStateFlow(adapter: SearchAdapter): Flow<LoadState> {
        return adapter.loadStateFlow.map { it.refresh }
    }

    private fun setupAppBar() {
        binding.etSearch.requestFocus()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
        with (binding) {
            ibBack.setOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
            ibClear.setOnClickListener {
                etSearch.text.clear()
                ibClear.visibility = View.GONE
            }
        }

    }

    private fun setupSearchInput() {
        binding.etSearch.addTextChangedListener {
            viewModel.setSearchBy(it.toString())
        }
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isEmpty()) {
                    viewModel.setSearchBy(SearchViewModel.BASE_SEARCH_VALUE)
                }
                binding.ibClear.visibility = View.VISIBLE
            }
        })
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}