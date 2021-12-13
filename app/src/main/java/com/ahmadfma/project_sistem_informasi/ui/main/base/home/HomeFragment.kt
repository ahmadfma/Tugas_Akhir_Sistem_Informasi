package com.ahmadfma.project_sistem_informasi.ui.main.base.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.HomeLayoutAdapter
import com.ahmadfma.project_sistem_informasi.ui.main.search.SearchFragment
import com.example.indonews_app.UserData.UserViewModel
import kotlinx.android.synthetic.main.fragment_home.*

const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private lateinit var homeLayoutAdapter: HomeLayoutAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        homeViewModel.fragment = this

        homeLayoutAdapter = HomeLayoutAdapter(this, homeViewModel, userViewModel)
        rcView_Home_Layout.setHasFixedSize(true)
        rcView_Home_Layout.layoutManager = LinearLayoutManager(requireContext())
        rcView_Home_Layout.adapter = homeLayoutAdapter
    }

    private fun initListener() {
        searchInput.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                val txt = searchInput.text.toString()
                if(txt.isNotEmpty()) {
                    searchAction(txt)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun searchAction(txt: String) {
        SearchFragment.searchInput = txt
        findNavController().navigate(R.id.action_baseFragment_to_searchFragment)
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}