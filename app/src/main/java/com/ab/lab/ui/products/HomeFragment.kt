package com.ab.lab.ui.products

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ab.lab.R
import com.ab.lab.api.BrandType
import com.ab.lab.api.FishFoodType
import com.ab.lab.api.KeyType
import com.ab.lab.api.Query
import com.ab.lab.core.BaseFragment
import com.ab.lab.core.extension.getViewModelExt
import com.ab.lab.core.extension.navigateExt
import com.ab.lab.core.extension.showSelectDialogExt
import com.ab.lab.data.ProductsResult
import com.ab.lab.databinding.FragmentHomeBinding
import com.google.android.material.chip.ChipGroup


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var pagedProductsViewModel: PagedProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val defaultQuery = Query(KeyType.FISHFOOD)
        pagedProductsViewModel = getViewModelExt { PagedProductsViewModel(defaultQuery) }
        bindState()
        bindFilter()
        addDividers()
        return binding.root
    }

    private fun bindState() {
        val adapter = ProductsAdapter()
        binding.recycler.adapter = adapter
        adapter.shotClickListener = { item, _ ->
            navigateExt(HomeFragmentDirections.actionNavHomeToNavProduct(item.key))
        }
        bindList(adapter)
    }

    private fun bindFilter() {
        binding.typeFoodChipGroup.setOnCheckedChangeListener { _: ChipGroup?, checkedId: Int ->
            val foodType = when(checkedId) {
                R.id.dryfoodChip -> FishFoodType.DRYFOOD
                R.id.wetfoodChip -> FishFoodType.WETFOOD
                R.id.livefoodChip -> FishFoodType.LIVEFOOD
                R.id.tastyChip -> FishFoodType.TASTY
                else -> null
            }
            filterByFoodType(foodType)
        }
    }

    private fun filterByFoodType(typefood: FishFoodType?) {
        val stateTypeFood = pagedProductsViewModel.state.value?.query?.typefood
        if (typefood != stateTypeFood) {
            binding.recycler.scrollToPosition(0)
            val query = Query(KeyType.FISHFOOD)
            query.typefood = typefood
            query.brand = pagedProductsViewModel.state.value?.query?.brand
            showProgress()
            pagedProductsViewModel.accept(UiAction.Search(query))
        }
    }

    private fun filterByBrand(brandType: BrandType) {
        val stateTypeFood = pagedProductsViewModel.state.value?.query?.typefood
        binding.recycler.scrollToPosition(0)
        val query = Query(KeyType.FISHFOOD)
        query.typefood = stateTypeFood
        query.brand = if (brandType == BrandType.EVERYTHING) null else brandType
        showProgress()
        pagedProductsViewModel.accept(UiAction.Search(query))
    }

    private fun bindList(adapter: ProductsAdapter) {
        setupScrollListener(pagedProductsViewModel.accept)

        pagedProductsViewModel.state.map(UiState::productsResult).distinctUntilChanged()
            .observe(viewLifecycleOwner) { result ->
                dissmissProgress()
                when (result) {
                    is ProductsResult.Success -> {
                        showEmptyList(result.data.isEmpty())
                        adapter.submitList(result.data)
                    }
                    is ProductsResult.Error -> {
                        showErrorSnackbar(R.string.error_app, R.string.snackbar_retry)
                        {
                            pagedProductsViewModel.retry()
                            showProgress()
                        }
                    }
                }
            }
    }

    private fun showEmptyList(show: Boolean) {
        binding.emptyList.isVisible = show
        binding.recycler.isVisible = !show
    }

    private fun setupScrollListener(onScrollChanged: (UiAction.Scroll) -> Unit) {
        val layoutManager = binding.recycler.layoutManager as LinearLayoutManager
        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                onScrollChanged(
                    UiAction.Scroll(visibleItemCount, lastVisibleItem, totalItemCount)
                )
            }
        })
    }

    private fun addDividers() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.recycler.addItemDecoration(decoration)
    }

    private fun showProgress() {
        binding.progressView.visibility = View.VISIBLE
        binding.mainLayout.alpha = 0.3f
    }

    private fun dissmissProgress() {
        binding.progressView.visibility = View.GONE
        binding.mainLayout.alpha = 1.0f
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_options_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.filter -> {
                val brands = BrandType.values().toList()
                BrandType.EVERYTHING.viewName = getString(R.string.brand_everything)
                showSelectDialogExt(brands, R.string.dialog_select_brand) { _, i ->
                    val brandTypes = BrandType.values()[i]
                    filterByBrand(brandTypes)
                }
                true
            }
            else -> false
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}