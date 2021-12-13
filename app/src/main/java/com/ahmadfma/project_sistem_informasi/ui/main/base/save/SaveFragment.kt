package com.ahmadfma.project_sistem_informasi.ui.main.base.save

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.data.konten
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.NewsAdapter
import com.ahmadfma.project_sistem_informasi.ui.main.detail.DetailNewsFragment
import com.bumptech.glide.Glide
import com.example.indonews_app.UserData.UserData_Berita
import com.example.indonews_app.UserData.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_save.*
import kotlinx.android.synthetic.main.item_latest_news.view.*

class SaveFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        return inflater.inflate(R.layout.fragment_save, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NewsAdapter(object : NewsAdapter.Listener {
            override fun onItemClick(berita: berita) {
                DetailNewsFragment.selectedBerita = berita
                findNavController().navigate(R.id.action_baseFragment_to_detailNewsFragment)
            }
        })

        rcView_list_saved_berita.setHasFixedSize(true)
        rcView_list_saved_berita.layoutManager = LinearLayoutManager(requireContext())
        rcView_list_saved_berita.adapter = adapter

        userViewModel.readAllData.observe(viewLifecycleOwner, Observer {
            for(id in it) {
                getBerita(id.berita_id)
            }
        })

    }

    private fun getBerita(id: String) {
        Firebase.firestore
            .collection("berita")
            .document(id)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val doc = it.result
                    val berita = doc!!.toObject(berita::class.java)
                    adapter.addData(berita!!)
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SaveFragment()
    }
}