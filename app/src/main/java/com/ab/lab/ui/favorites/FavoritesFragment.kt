package com.ab.lab.ui.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import com.ab.lab.core.BaseFragment
import com.ab.lab.core.extension.getActivityViewModelExt
import com.ab.lab.core.extension.getViewModelExt
import com.ab.lab.core.extension.navigateExt
import com.ab.lab.databinding.FragmentFavoritesBinding
import com.ab.lab.ui.activity.MainViewModel

class FavoritesFragment : BaseFragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoritesViewModel: FavoritesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val userId = getActivityViewModelExt { MainViewModel() }.account?.id
        userId?.let { u ->
            favoritesViewModel = getViewModelExt { FavoritesViewModel(u) }
            initRecycler()
            favoritesViewModel.load()
        }
        return binding.root
    }

    private fun initRecycler() {
        val recyclerView = binding.list
        val adapter = FavoritesAdapter()
        recyclerView.adapter = adapter
        addDividers()

        adapter.shotClickListener = { item, _ ->
            navigateExt(FavoritesFragmentDirections.actionNavFavoritesToNavProduct(item.productKey))
        }

        favoritesViewModel.favorites.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
                if (it.isEmpty()) {
                    binding.list.visibility = View.GONE
                    binding.emptyList.visibility = View.VISIBLE
                } else {
                    binding.list.visibility = View.VISIBLE
                    binding.emptyList.visibility = View.GONE
                }
            }
        }

        val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, LEFT or RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.currentList[viewHolder.adapterPosition]
                favoritesViewModel.deleteFavorites(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun addDividers() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.list.addItemDecoration(decoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}