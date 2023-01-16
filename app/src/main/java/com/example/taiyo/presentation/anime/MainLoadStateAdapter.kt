package com.example.taiyo.presentation.anime

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.taiyo.databinding.FooterLoadStateBinding
import com.example.taiyo.databinding.MainLoadStateBinding

typealias RefreshAction = () -> Unit

class MainLoadStateAdapter(
    private val refreshAction: RefreshAction,
) : LoadStateAdapter<MainLoadStateAdapter.FooterLoaderViewHolder>() {

    override fun onBindViewHolder(holder: FooterLoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState,
    ): FooterLoaderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FooterLoadStateBinding.inflate(inflater, parent, false)
        return FooterLoaderViewHolder(binding, null, refreshAction)
    }

    class FooterLoaderViewHolder(
        private val binding: FooterLoadStateBinding,
        private val swipeRefreshLayout: SwipeRefreshLayout?,
        private val refreshAction: RefreshAction,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.refreshButton.setOnClickListener { refreshAction() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.isRefreshing = false
                progressBar.isVisible = loadState is LoadState.Loading
            } else {
                progressBar.isVisible = loadState is LoadState.Loading
            }
            tvLoadError.isVisible = loadState is LoadState.Error
            tvLoadErrorInfo.isVisible = loadState is LoadState.Error
            refreshButton.isVisible = loadState is LoadState.Error
        }
    }

    class MainLoaderViewHolder(
        private val binding: MainLoadStateBinding,
        private val swipeRefreshLayout: SwipeRefreshLayout?,
        private val refreshAction: RefreshAction,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.refreshButton.setOnClickListener { refreshAction() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.isRefreshing = false
                progressBar.isVisible = loadState is LoadState.Loading
            } else {
                progressBar.isVisible = loadState is LoadState.Loading
            }
            ivIcon.isVisible = loadState is LoadState.Error
            tvLoadError.isVisible = loadState is LoadState.Error
            tvLoadErrorInfo.isVisible = loadState is LoadState.Error
            refreshButton.isVisible = loadState is LoadState.Error
        }
    }
}