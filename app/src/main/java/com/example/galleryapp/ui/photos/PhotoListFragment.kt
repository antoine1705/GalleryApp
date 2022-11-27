package com.example.galleryapp.ui.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.galleryapp.R
import com.example.galleryapp.databinding.FragmentPhotoListBinding
import com.example.galleryapp.ui.adapter.GalleryAdapter
import com.example.galleryapp.ui.detail.PhotoFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhotoListFragment : Fragment() {

    companion object {
        const val FOLDER_NAME_KEY = "folder_name_key"
    }

    // Lazy inject ViewModel
    private val viewModel by viewModel<PhotoListViewModel>()

    private var _binding: FragmentPhotoListBinding? = null

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
        _binding = FragmentPhotoListBinding.inflate(inflater, container, false)
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
        binding.toolbar.title = folderName
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val layoutManager = GridLayoutManager(requireContext(), 4)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return adapter.getSpanCount(position)
            }
        }
        binding.rvPhotos.layoutManager = layoutManager
        binding.rvPhotos.adapter = adapter
    }

    private fun createAdapter(): GalleryAdapter {
        return GalleryAdapter {
            val bundle = Bundle()
            bundle.putString(PhotoFragment.PHOTO_URL_KEY, it.url)
            bundle.putString(PhotoFragment.FOLDER_NAME_KEY, it.folderName)
            findNavController().navigate(R.id.action_PhotoListFragment_to_photoFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}