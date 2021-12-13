package com.ahmadfma.project_sistem_informasi.ui.main.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.ui.main.base.covid.CovidFragment
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.HomeFragment
import com.ahmadfma.project_sistem_informasi.ui.main.base.information.InformationFragment
import com.ahmadfma.project_sistem_informasi.ui.main.base.save.SaveFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_base.*

class BaseFragment : Fragment() {

    private lateinit var viewmodel: BaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVariable()
        initListener()
    }

    private fun initVariable() {
        viewmodel = ViewModelProvider(this).get(BaseViewModel::class.java)
    }

    private fun initListener() {
        base_bottom_nav.setOnNavigationItemSelectedListener(navListener())
        base_bottom_nav.selectedItemId = viewmodel.selectedId
    }

    private fun navListener(): BottomNavigationView.OnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId) {
            R.id.nav_home -> {
                loadHomeFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_covid -> {
                loadCovidFragment()
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_like -> {
                loadLikeFragment()
                return@OnNavigationItemSelectedListener true
            }
//            R.id.nav_info -> {
//                loadInfoFragment()
//                return@OnNavigationItemSelectedListener true
//            }
        }
        false
    }

    private fun loadHomeFragment() {
        viewmodel.selectedId = R.id.nav_home
        val fManager = activity?.supportFragmentManager
        fManager?.beginTransaction()
            ?.replace(R.id.base_fragment_container, HomeFragment())
            ?.commit()
    }

    private fun loadCovidFragment() {
        viewmodel.selectedId = R.id.nav_covid
        val fManager = activity?.supportFragmentManager
        fManager?.beginTransaction()
            ?.replace(R.id.base_fragment_container, CovidFragment())
            ?.commit()
    }

    private fun loadLikeFragment() {
        viewmodel.selectedId = R.id.nav_like
        val fManager = activity?.supportFragmentManager
        fManager?.beginTransaction()
            ?.replace(R.id.base_fragment_container, SaveFragment())
            ?.commit()
    }

    private fun loadInfoFragment() {
//        viewmodel.selectedId = R.id.nav_info
//        val fManager = activity?.supportFragmentManager
//        fManager?.beginTransaction()
//            ?.replace(R.id.base_fragment_container, InformationFragment())
//            ?.commit()
    }

    companion object {

        @JvmStatic
        fun newInstance() = BaseFragment()
    }
}