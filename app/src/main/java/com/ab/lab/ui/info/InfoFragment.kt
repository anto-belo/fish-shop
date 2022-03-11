package com.ab.lab.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ab.lab.R
import com.ab.lab.core.BaseFragment
import com.ab.lab.databinding.FragmentInfoBinding

class InfoFragment : BaseFragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        initPages()
        return binding.root
    }

    private fun initPages() {
        val pages: Array<String> = resources.getStringArray(R.array.help_pages_array)
        val adapter = InfoPagerAdapter(pages)
        binding.viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}