package com.example.galleryapp.ui.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentVideoListBinding
import com.example.galleryapp.ui.adapter.GalleryAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class VideoListFragment : Fragment() {

    companion object {
        const val FOLDER_NAME_KEY = "folder_name_key"
    }

    // Lazy inject ViewModel
    private val viewModel by viewModel<VideoListViewModel>()

    private var _binding: FragmentVideoListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter by lazy {
        createAdapter()
    }

    private val folderName
        get() = arguments?.getString(FOLDER_NAME_KEY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.loadImages(folderName)
        viewModel.imagesObs.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
        binding.rvVideos.adapter = adapter
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun createAdapter(): GalleryAdapter {
        return GalleryAdapter {
            val bundle = Bundle()
            bundle.putString(VideoFragment.VIDEO_URL_KEY, it.url)
            findNavController().navigate(R.id.action_videoListFragment_to_videoFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}