package com.taiyoapp.taiyo.anime.presentation.fragment

import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.taiyoapp.taiyo.MainActivity
import com.taiyoapp.taiyo.R
import com.taiyoapp.taiyo.anime.domain.entity.Screenshot
import com.taiyoapp.taiyo.anime.presentation.adapter.ScreenshotsVpAdapter
import com.taiyoapp.taiyo.databinding.FragmentScreenshotsBinding

class ScreenshotsFragment : Fragment() {
    private var screenshots: List<Screenshot> = UNDEFINED_SCREENSHOTS
    private var position: Int = UNDEFINED_POSITION

    private lateinit var screenshotsVpAdapter: ScreenshotsVpAdapter

    private var _binding: FragmentScreenshotsBinding? = null
    private val binding: FragmentScreenshotsBinding
        get() = _binding ?: throw RuntimeException("FragmentScreenshotsBinding is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScreenshotsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBarColor()
        hideBottomNavigationBar()
        setupScreenshotsAdapter()
        setupViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupStatusBarColor() {
        requireActivity().window.statusBarColor = ContextCompat.getColor(
            requireContext(), R.color.black
        )
    }

    private fun hideBottomNavigationBar() {
        val bottomNavigationView = (context as MainActivity).findViewById<BottomNavigationView>(
            R.id.main_bottom_bar
        )
        bottomNavigationView.visibility = View.GONE
    }

    private fun setupScreenshotsAdapter() {
        screenshotsVpAdapter = ScreenshotsVpAdapter(requireContext())
        screenshotsVpAdapter.submitList(screenshots)
    }

    private fun setupViewPager() {
        with(binding) {
            viewPager2.adapter = screenshotsVpAdapter
            viewPager2.setCurrentItem(position, false)
        }
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (args.containsKey(POSITION) && args.containsKey(SCREENSHOTS)) {
            args.getParcelableArrayList<Screenshot?>(SCREENSHOTS)?.let {
                screenshots = it
            }
            position = args.getInt(POSITION)
        } else {
            throw RuntimeException("Args doesn't contain a key")
        }
    }

    inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelable(key) as? T
    }

    companion object {
        private const val POSITION = "position"
        private const val SCREENSHOTS = "screenshots"

        private const val UNDEFINED_POSITION = 0
        private val UNDEFINED_SCREENSHOTS = listOf<Screenshot>()

        fun newInstance(screenshot: List<Screenshot>, position: Int): ScreenshotsFragment {
            return ScreenshotsFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(SCREENSHOTS, ArrayList(screenshot))
                    putInt(POSITION, position)
                }
            }
        }
    }
}