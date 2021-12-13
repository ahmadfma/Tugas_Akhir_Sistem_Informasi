package com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.LIST_CATEGORY
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.data.konten
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.HomeViewModel
import com.ahmadfma.project_sistem_informasi.utilities.DateHelper
import com.ahmadfma.project_sistem_informasi.utilities.POST
import com.bumptech.glide.Glide
import com.example.indonews_app.UserData.UserData_Berita
import com.example.indonews_app.UserData.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_list_news.view.*
import kotlinx.android.synthetic.main.item_news_trending.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrendingAdapter(private val fragment: Fragment, private val userViewModel: UserViewModel, private val listener: Listener): RecyclerView.Adapter<TrendingAdapter.Holder>() {

    interface Listener {
        fun onItemClick(berita: berita)
    }

    val trending = mutableListOf<berita>()

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(berita: berita) {
            with(itemView) {
                isSaved(berita)

                getImage(berita, this)
                getTime(berita, this)
                newsTitle.text = berita.judul
                getCategoryColor(berita, this)
                categoryTxtNews.text = berita.kategori

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newsDesk.text = Html.fromHtml(berita.isi, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    newsDesk.text = Html.fromHtml(berita.isi)
                }

                if(berita.viewer != null) {
                    trendingViewerCount.text = berita.viewer
                } else {
                    trendingViewerCount.text = "0"
                }

                penerbitBerita.text = berita.penerbit
                this.setOnClickListener {
                    listener.onItemClick(berita)
                }

                trendingBookmarkBtn.setOnClickListener {
                    if(trendingBookmarkBtn.tag == "saved") {
                        deleteBerita(berita)
                    }
                    else {
                        saveBerita(berita)
                    }
                }
            }
        }

        private fun getImage(berita: berita, view: View) {
            val images = mutableListOf<konten>()

            Firebase.firestore
                .collection("berita")
                .document(berita.id!!)
                .collection("konten")
                .get()
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        for(doc in it.result!!) {
                            images.add(doc.toObject(konten::class.java))
                        }
                        with(view) {
                            Glide.with(context)
                                .load(images[0].url)
                                .into(newsImage)
                        }

                    }
                }
        }

        private fun getTime(berita: berita, view: View) {
            var date = berita.tanggal!!
            var dateText = DateHelper.findTimeDifference(date.removeRange(date.length-3, date.length), POST)
            view.waktu.text = " . $dateText"
        }

        private fun getCategoryColor(berita: berita, view: View) {
            with(view) {
                when(berita.kategori) {
                    LIST_CATEGORY[1] -> setColor(categoryBackgroundTrending, R.color.Bisnis)
                    LIST_CATEGORY[2] -> setColor(categoryBackgroundTrending, R.color.Science)
                    LIST_CATEGORY[3] -> setColor(categoryBackgroundTrending, R.color.Sport)
                    LIST_CATEGORY[4] -> setColor(categoryBackgroundTrending, R.color.Otomotif)
                    LIST_CATEGORY[5] -> setColor(categoryBackgroundTrending, R.color.Edukasi)
                    LIST_CATEGORY[6] -> setColor(categoryBackgroundTrending, R.color.Travel)
                    LIST_CATEGORY[7] -> setColor(categoryBackgroundTrending, R.color.Health)
                    LIST_CATEGORY[8] -> setColor(categoryBackgroundTrending, R.color.Lifestyle)
                    LIST_CATEGORY[9] -> setColor(categoryBackgroundTrending, R.color.Teknologi)
                }
            }
        }

        private fun setColor(layout: View, colorId: Int) {
            layout.background.setTint(layout.resources.getColor(colorId));
        }

        private fun isSaved(berita: berita) {
            with(itemView) {
                fragment.lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        userViewModel.readAllData.observe(fragment.viewLifecycleOwner, Observer {
                            for(id in it) {
                                if(id.berita_id == berita.id!!) {
                                    trendingBookmarkBtn.setImageResource(R.drawable.ic_bookmark_pressed)
                                    trendingBookmarkBtn.tag = "saved"
                                    return@Observer
                                }
                            }
                        })
                    }
                }
            }
        }

        private fun saveBerita(berita: berita) {
            with(itemView) {
                userViewModel.addArticle(UserData_Berita(0, berita.id!!))
                trendingBookmarkBtn.setImageResource(R.drawable.ic_bookmark_pressed)
                trendingBookmarkBtn.tag = "saved"
            }
        }

        private fun deleteBerita(berita: berita) {
            with(itemView) {
                userViewModel.deleteSpecificArticleFromDatabase(berita.id!!)
                trendingBookmarkBtn.setImageResource(R.drawable.ic_bookmark2)
                trendingBookmarkBtn.tag = "unsaved"
            }
        }

    }

    fun setList(berita: MutableList<berita>) {
        this.trending.addAll(berita)
        notifyDataSetChanged()
    }

    fun addData(berita: berita) {
        this.trending.add(berita)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_trending, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(trending[position])
    }

    override fun getItemCount(): Int {
        return trending.size
    }

}