package com.ahmadfma.project_sistem_informasi.ui.main.base.home

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.ahmadfma.project_sistem_informasi.data.TrendingId
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.NewsAdapter
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.TrendingAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_layout_home_trending.view.*


class HomeViewModel: ViewModel() {
    private var TAG = "HomeViewModel"
    var fragment: HomeFragment? = null
    
    var listTrendingId = mutableListOf<String>()
    var listTrendingNews = mutableListOf<berita>()

    var listNews = mutableListOf<berita>()

    // TRENDING

    fun getTrendingListId(adapter: TrendingAdapter, view: View) {
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
                    getTrendingNews(adapter, view)
                }
            }
    }

    private fun getTrendingNews(adapter: TrendingAdapter, view: View) {
        var checkPoint = 0
        val livedata = MutableLiveData<Boolean>()
        livedata.observe(fragment!!.viewLifecycleOwner, {
            if(checkPoint == listTrendingId.size) {
                adapter.setList(listTrendingNews)
                view.trending_view_pager.adapter = adapter
                view.trending_view_pager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                view.circle_indicator.setViewPager(view.trending_view_pager)
            }
        })

        for(id in listTrendingId) {
            Firebase.firestore
                .collection("berita")
                .whereEqualTo("id", id)
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        for(doc in it.result!!) {
                            val berita = doc.toObject(berita::class.java)
                            listTrendingNews.add(berita)
                            checkPoint++
                            Log.d(TAG, "getTrendingNews: ${berita.id} is loaded")
                            livedata.value = true
                        }
                    }
                }
        }
    }

    //LATEST NEWS

    fun getAllNews(adapter: NewsAdapter, view: View) {
        var maxNews = NewsAdapter.maxNews

        Firebase.firestore
            .collection("berita")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    for(doc in it.result!!) {
                        if(maxNews != 0) {
                            val berita = doc.toObject(berita::class.java)
                            listNews.add(berita)
                            adapter.addData(berita)
                            maxNews--
                        } else {
                            return@addOnCompleteListener
                        }
                    }
                }
            }
    }

}