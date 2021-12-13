package com.ahmadfma.project_sistem_informasi.ui.main.base.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.Kategori
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.NewsAdapter
import com.ahmadfma.project_sistem_informasi.ui.main.detail.DetailNewsFragment
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_categoried_news.*

class CategoriedNewsFragment : Fragment() {
    private val TAG = "CategoriedNewsFragment"
    private lateinit var adapter: NewsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categoried_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbarCategory.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        Log.d(TAG, "kategori : $selectedCategory")
        category.text = selectedCategory

        adapter = NewsAdapter(object : NewsAdapter.Listener {
            override fun onItemClick(berita: berita) {
                DetailNewsFragment.selectedBerita = berita
                findNavController().navigate(R.id.action_categoriedNewsFragment_to_detailNewsFragment)
            }
        })

        rcView_category_news.setHasFixedSize(true)
        rcView_category_news.layoutManager = LinearLayoutManager(requireContext())
        rcView_category_news.adapter = adapter

        Firebase.firestore
            .collection("berita")
            .whereEqualTo("kategori", selectedCategory)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    Log.d(TAG, "result : ${it.result!!.size()}")
                    for(doc in it.result!!) {
                        val berita = doc.toObject(berita::class.java)
                        adapter.addData(berita)
                    }
                }
            }
            .addOnFailureListener {
                Log.d(TAG, it.message!!)
            }

        Firebase.firestore
            .collection("kategori")
            .document(selectedCategory)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val categ = it.result!!.toObject(Kategori::class.java)
                    Glide.with(requireContext())
                        .load(categ!!.thumbnail)
                        .into(categoryThumb)
                }
            }

    }

    companion object {

        @JvmStatic
        fun newInstance() = CategoriedNewsFragment()
        var selectedCategory = ""
    }
}