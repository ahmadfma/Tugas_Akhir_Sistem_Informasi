package com.ahmadfma.project_sistem_informasi.ui.main.List

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.LIST_CATEGORY
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.data.konten
import com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter.NewsAdapter
import com.ahmadfma.project_sistem_informasi.utilities.DateHelper
import com.ahmadfma.project_sistem_informasi.utilities.POST
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_list_news.view.*
import android.graphics.PorterDuff
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.ahmadfma.project_sistem_informasi.ui.main.detail.DetailNewsFragment
import com.example.indonews_app.UserData.UserData_Berita
import com.example.indonews_app.UserData.UserViewModel
import kotlinx.android.synthetic.main.fragment_detail_news.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class BeritaAdapter(private val fragment: Fragment, private val userViewModel: UserViewModel, private val listener: Listener): RecyclerView.Adapter<BeritaAdapter.Holder>() {
    private var TAG = "NewsAdapter"

    interface Listener {
        fun onItemClick(berita: berita)
    }

    var news = mutableListOf<berita>()

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(berita: berita, listener: Listener) {
            with(itemView) {
                isSaved(berita)
                getTime(berita, this)
                getImage(berita, this)
                getCategoryColor(berita, this)
                beritaTitle.text = berita.judul
                beritaSource.text = berita.penerbit
                categoryTxt.text = berita.kategori

                if(berita.viewer != null) {
                    viewerCount.text = berita.viewer
                } else {
                    viewerCount.text = "0"
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    beritaIsi.text = Html.fromHtml(berita.isi, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    beritaIsi.text = Html.fromHtml(berita.isi)
                }

                this.setOnClickListener {
                    listener.onItemClick(berita)
                }

                listBookmarkBtn.setOnClickListener {
                    if(listBookmarkBtn.tag == "saved") {
                        deleteBerita(berita)
                    }
                    else {
                        saveBerita(berita)
                    }
                }
            }
        }

        private fun getCategoryColor(berita: berita, view: View) {
            with(view) {
                when(berita.kategori) {
                    LIST_CATEGORY[1] -> setColor(categoryBackground, R.color.Bisnis)
                    LIST_CATEGORY[2] -> setColor(categoryBackground, R.color.Science)
                    LIST_CATEGORY[3] -> setColor(categoryBackground, R.color.Sport)
                    LIST_CATEGORY[4] -> setColor(categoryBackground, R.color.Otomotif)
                    LIST_CATEGORY[5] -> setColor(categoryBackground, R.color.Edukasi)
                    LIST_CATEGORY[6] -> setColor(categoryBackground, R.color.Travel)
                    LIST_CATEGORY[7] -> setColor(categoryBackground, R.color.Health)
                    LIST_CATEGORY[8] -> setColor(categoryBackground, R.color.Lifestyle)
                    LIST_CATEGORY[9] -> setColor(categoryBackground, R.color.Teknologi)
                }
            }
        }

        private fun setColor(layout: View, colorId: Int) {
            layout.background.setTint(layout.resources.getColor(colorId));
        }

        private fun getTime(berita: berita, view: View) {
            var date = berita.tanggal!!
            var dateText = DateHelper.findTimeDifference(date.removeRange(date.length-3, date.length), POST)
            view.beritaDate.text = "$dateText"
            Log.d(TAG, "getTime: $dateText")
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
                                .into(beritaImg)
                        }

                    }
                }
        }

        private fun isSaved(berita: berita) {
            with(itemView) {
                fragment.lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        userViewModel.readAllData.observe(fragment.viewLifecycleOwner, Observer {
                            for(id in it) {
                                if(id.berita_id == berita.id!!) {
                                    listIconBookmarkBtn.setImageResource(R.drawable.ic_bookmark_pressed)
                                    listBookmarkBtn.tag = "saved"
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
                listIconBookmarkBtn.setImageResource(R.drawable.ic_bookmark_pressed)
                listBookmarkBtn.tag = "saved"
            }
        }

        private fun deleteBerita(berita: berita) {
            with(itemView) {
                userViewModel.deleteSpecificArticleFromDatabase(berita.id!!)
                listIconBookmarkBtn.setImageResource(R.drawable.ic_bookmark)
                listBookmarkBtn.tag = "unsaved"
            }
        }

    }


    fun addData(berita: berita) {
        news.add(berita)
        notifyDataSetChanged()
    }

    fun setData(list : MutableList<berita>) {
        this.news = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_news, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(news[position], listener)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    companion object {
        @JvmStatic
        var maxNews = 5
    }

}