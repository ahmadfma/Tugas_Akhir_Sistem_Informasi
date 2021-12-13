package com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.ui.main.List.BERITA_TERKINI
import com.ahmadfma.project_sistem_informasi.ui.main.List.BERITA_TRENDING
import com.ahmadfma.project_sistem_informasi.ui.main.List.ListBeritaFragment
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.HomeFragment
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.HomeViewModel
import com.ahmadfma.project_sistem_informasi.ui.main.detail.DetailNewsFragment
import com.example.indonews_app.UserData.UserViewModel
import kotlinx.android.synthetic.main.item_layout_home_kategori.view.*
import kotlinx.android.synthetic.main.item_layout_home_news.view.*
import kotlinx.android.synthetic.main.item_layout_home_trending.view.*

const val CATEGORY = 0
const val TRENDING = 1
const val NEWS = 2

class HomeLayoutAdapter(private val fragment: HomeFragment, private val homeViewModel: HomeViewModel, private val userViewModel: UserViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val homeLayout = arrayListOf(
        CATEGORY, TRENDING, NEWS
    )

    inner class KategoriHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                rcView_kategori.setHasFixedSize(true)
                rcView_kategori.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                rcView_kategori.adapter = KategoriAdapter(fragment)
            }
        }
    }

    inner class TrendingNewsHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                trendingBtn.setOnClickListener {
                    ListBeritaFragment.actionSelected = BERITA_TRENDING
                    fragment.findNavController().navigate(R.id.action_baseFragment_to_listBeritaFragment)
                }
                val adapter = TrendingAdapter(fragment, userViewModel, object : TrendingAdapter.Listener {
                    override fun onItemClick(berita: berita) {
                        DetailNewsFragment.selectedBerita = berita
                        fragment.findNavController().navigate(R.id.action_baseFragment_to_detailNewsFragment)
                    }
                })
                homeViewModel.getTrendingListId(adapter, this)
            }
        }
    }

    inner class NewsHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {

                beritaTerkiniBtn.setOnClickListener {
                    ListBeritaFragment.actionSelected = BERITA_TERKINI
                    fragment.findNavController().navigate(R.id.action_baseFragment_to_listBeritaFragment)
                }

                val adapter = NewsAdapter(object : NewsAdapter.Listener {
                    override fun onItemClick(berita: berita) {
                        DetailNewsFragment.selectedBerita = berita
                        fragment.findNavController().navigate(R.id.action_baseFragment_to_detailNewsFragment)
                    }
                })
                rcView_news.setHasFixedSize(true)
                rcView_news.layoutManager = LinearLayoutManager(context)
                rcView_news.adapter = adapter

                homeViewModel.getAllNews(adapter, this)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> CATEGORY
            1 -> TRENDING
            else -> NEWS
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            CATEGORY -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_home_kategori, parent, false)
                KategoriHolder(view)
            }
            TRENDING -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_home_trending, parent, false)
                TrendingNewsHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_home_news, parent, false)
                NewsHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            CATEGORY -> {
                (holder as KategoriHolder).bind()
            }
            TRENDING -> {
                (holder as TrendingNewsHolder).bind()
            }
            NEWS -> {
                (holder as NewsHolder).bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return homeLayout.size
    }


}