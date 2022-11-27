package com.example.galleryapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentFavoritePhotoListBinding
import com.example.galleryapp.ui.adapter.GalleryAdapter
import com.example.galleryapp.ui.detail.PhotoFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritePhotoListFragment : Fragment() {

    // Lazy inject ViewModel
    private val viewModel by viewModel<FavoritePhotoListViewModel>()

    private var _binding: FragmentFavoritePhotoListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter by lazy {
        createAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritePhotoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        viewModel.loadFavoritePhotos()
        viewModel.photosObs.observe(viewLifecycleOwner) {
            adapter.updateItems(it)
        }
        binding.rvPhotos.adapter = adapter
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun createAdapter(): GalleryAdapter {
        return GalleryAdapter {
            val bundle = Bundle()
            bundle.putString(PhotoFragment.PHOTO_URL_KEY, it.url)
            bundle.putString(PhotoFragment.FOLDER_NAME_KEY, it.folderName)
            findNavController().navigate(R.id.action_favoritePhotoListFragment_to_photoFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}