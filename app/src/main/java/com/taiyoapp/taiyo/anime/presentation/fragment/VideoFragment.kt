package com.taiyoapp.taiyo.anime.presentation.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.Video
import com.taiyoapp.taiyo.anime.presentation.adapter.VideoAdapter
import com.taiyoapp.taiyo.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {
    private var videoKind: String = UNDEFINED_VIDEO_KIND
    private var video: List<Video> = UNDEFINED_VIDEO

    private var _binding: FragmentVideoBinding? = null
    private val binding: FragmentVideoBinding
        get() = _binding ?: throw RuntimeException("FragmentVideoBinding is null")

    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        // status bar color
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(), R.color.ui_bg
        )
    }

    private fun setupAppBar() {
        with (binding) {
            ibBack.setOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
            tvVideoKind.text = videoKind
        }
    }

    private fun setupRecyclerView() {
        videoAdapter = VideoAdapter(requireContext())
        videoAdapter.submitList(video)
        videoAdapter.onVideoClick = {
            val intentApp = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            val intentBrowser = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
            try {
                this.startActivity(intentApp)
            } catch (ex: ActivityNotFoundException) {
                this.startActivity(intentBrowser)
            }
        }
        with (binding) {
            rvVideo.adapter = videoAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (args.containsKey(VIDEO_KIND) && args.containsKey(VIDEO)) {
            videoKind = args.getString(
                VIDEO_KIND
            ) ?: requireContext().getString(R.string.video)
            args.getParcelableArrayList<Video?>(VIDEO)?.let {
                video = it
            }
        } else {
            throw RuntimeException("Args doesn't contain a key")
        }
    }

    companion object {
        private const val VIDEO_KIND = "video_kind"
        private const val VIDEO = "video"
        private const val UNDEFINED_VIDEO_KIND = ""
        private val UNDEFINED_VIDEO = listOf<Video>()

        fun newInstance(kind: String, video: List<Video>): VideoFragment {
            return VideoFragment().apply {
                arguments = Bundle().apply {
                    putString(VIDEO_KIND, kind)
                    putParcelableArrayList(VIDEO, ArrayList(video))
                }
            }
        }
    }
}