package com.ahmadfma.project_sistem_informasi.ui.main.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.NewsAdapter
import com.ahmadfma.project_sistem_informasi.ui.main.detail.DetailNewsFragment
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbarHasilPencarian.subtitle = "Mencari : $searchInput"

        toolbarHasilPencarian.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter = NewsAdapter(object : NewsAdapter.Listener {
            override fun onItemClick(berita: berita) {
                DetailNewsFragment.selectedBerita = berita
                findNavController().navigate(R.id.action_searchFragment_to_detailNewsFragment)
            }
        })

        rcView_result.setHasFixedSize(true)
        rcView_result.layoutManager = LinearLayoutManager(requireContext())
        rcView_result.adapter = adapter

        Firebase.firestore
            .collection("berita")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    for(doc in it.result!!) {
                        val berita = doc.toObject(berita::class.java)
                        if(berita.judul!!.contains(searchInput, ignoreCase = true)) {
                            adapter.addData(berita)
                        }
                    }
                }
            }
            .addOnFailureListener {
                Log.d("Search Result", it.message!!)
            }

    }

    companion object {

        @JvmStatic
        fun newInstance() = SearchFragment()
        var searchInput = ""
    }
}