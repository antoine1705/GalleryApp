package com.example.galleryapp.ui.folder

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentGalleryFolderBinding
import com.example.galleryapp.ui.adapter.GalleryFolderAdapter
import com.example.galleryapp.ui.photos.PhotoListFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FolderFragment : Fragment() {

    // Lazy inject ViewModel
    private val viewModel by viewModel<FolderViewModel>()

    private var _binding: FragmentGalleryFolderBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter by lazy {
        createAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val registerPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission())
            { isGranted ->
                if (isGranted) {
                    viewModel.loadImages()
                }
            }
        registerPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.imagesObs.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
        binding.rvFolderPhotos.adapter = adapter
        binding.tvFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_FolderFragment_to_favoritePhotoListFragment)
        }
        binding.tvVideos.setOnClickListener {
            findNavController().navigate(R.id.action_FolderFragment_to_videoListFragment)
        }
    }

    private fun createAdapter(): GalleryFolderAdapter {
        return GalleryFolderAdapter {
            val bundle = Bundle()
            bundle.putString(PhotoListFragment.FOLDER_NAME_KEY, it)
            findNavController().navigate(R.id.action_FolderFragment_to_PhotoListFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}