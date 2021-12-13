package com.ahmadfma.project_sistem_informasi.ui.main.List

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.TrendingId
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.TrendingAdapter
import com.ahmadfma.project_sistem_informasi.ui.main.detail.DetailNewsFragment
import com.example.indonews_app.UserData.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_list_berita.*
import kotlinx.android.synthetic.main.item_layout_home_trending.view.*

const val BERITA_TERKINI = "Berita Terkini"
const val BERITA_TRENDING = "Trending"

class ListBeritaFragment : Fragment() {
    private val TAG = "ListBeritaFragment"

    private lateinit var adapter: BeritaAdapter
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        return inflater.inflate(R.layout.fragment_list_berita, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarListFrag.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        adapter = BeritaAdapter(this, userViewModel, object : BeritaAdapter.Listener {
            override fun onItemClick(berita: berita) {
                DetailNewsFragment.selectedBerita = berita
                findNavController().navigate(R.id.action_listBeritaFragment_to_detailNewsFragment)
            }
        })

        rcView_list_berita.setHasFixedSize(true)
        rcView_list_berita.layoutManager = LinearLayoutManager(requireContext())
        rcView_list_berita.adapter = adapter

        if(actionSelected == BERITA_TERKINI) {
            toolbarListFrag.title = BERITA_TERKINI
            Firebase.firestore
                .collection("berita")
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        for(doc in it.result!!) {
                            val berita = doc.toObject(berita::class.java)
                            adapter.addData(berita)
                        }
                    }
                }
        } else {
            toolbarListFrag.title = BERITA_TRENDING
            getTrendingListId()
        }
    }

    private var listTrendingId = mutableListOf<String>()
    private fun getTrendingListId() {
        Firebase.firestore
            .collection("trending")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    listTrendingId.clear()
                    for(doc in it.result!!) {
                        val obj = doc.toObject(TrendingId::class.java)
                        listTrendingId.add(obj.id!!)
                    }
                    Log.d(TAG, "getTrendingListId: $listTrendingId")
                    getTrendingNews()
                }
            }
    }

    private fun getTrendingNews() {
        for(id in listTrendingId) {
            Firebase.firestore
                .collection("berita")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        for(doc in it.result!!) {
                            val berita = doc.toObject(berita::class.java)
                            adapter.addData(berita)
                        }
                    }
                }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = ListBeritaFragment()
        var actionSelected = ""
    }
}