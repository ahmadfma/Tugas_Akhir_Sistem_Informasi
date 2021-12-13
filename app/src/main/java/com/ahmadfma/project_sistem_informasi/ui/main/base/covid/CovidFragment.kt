package com.ahmadfma.project_sistem_informasi.ui.main.base.covid

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.ui.main.base.covid.adapter.CovidLayoutAdapter
import kotlinx.android.synthetic.main.fragment_covid.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CovidFragment : Fragment() {

    private lateinit var viewModel: CovidViewModel
    private lateinit var covidLayoutAdapter: CovidLayoutAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_covid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariable()
        initListener()
        initData()
    }

    private fun initVariable() {
        viewModel = ViewModelProvider(this).get(CovidViewModel::class.java)
        covidLayoutAdapter = CovidLayoutAdapter(this, viewModel)
    }

    private fun initListener() {

    }

    private fun initData() {
        lifecycleScope.launch {
            if(fetchAllDataFromAPI()) {
                setCovidLayout()
            }
        }
    }

    private fun setCovidLayout() {
        rcView_covid_layout.setHasFixedSize(true)
        rcView_covid_layout.layoutManager = LinearLayoutManager(requireContext())
        rcView_covid_layout.adapter = covidLayoutAdapter
    }

    private suspend fun fetchAllDataFromAPI(): Boolean {
        var status = false
        lifecycleScope.async(Dispatchers.IO) {
            val job1 = async { viewModel.getTotalKasusFromAPI() }.await()
            val job2 = async { viewModel.getKasusBaruFromAPI() }.await()
            val job3 = async { viewModel.getDataProvinsiFromAPI() }.await()

            if(job1 && job2 && job3) {
                status = true
            }
        }.await()
        return status
    }

    companion object {
        @JvmStatic
        fun newInstance() = CovidFragment()
    }
}