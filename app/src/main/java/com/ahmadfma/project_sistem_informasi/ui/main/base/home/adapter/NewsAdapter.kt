package com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.data.konten
import com.ahmadfma.project_sistem_informasi.utilities.DateHelper
import com.ahmadfma.project_sistem_informasi.utilities.POST
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_latest_news.view.*
import kotlinx.android.synthetic.main.item_latest_news.view.newsDesk
import kotlinx.android.synthetic.main.item_latest_news.view.newsTitle
import kotlinx.android.synthetic.main.item_latest_news.view.penerbitBerita
import kotlinx.android.synthetic.main.item_news_trending.view.*

class NewsAdapter(private val listener: Listener): RecyclerView.Adapter<NewsAdapter.Holder>() {
    private var TAG = "NewsAdapter"

    interface Listener {
        fun onItemClick(berita: berita)
    }

    var news = mutableListOf<berita>()

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(berita: berita, listener: Listener) {
            with(itemView) {
                getTime(berita, this)
                getImage(berita, this)
                newsTitle.text = berita.judul
                penerbitBerita.text = berita.penerbit

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    newsDesk.text = Html.fromHtml(berita.isi, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    newsDesk.text = Html.fromHtml(berita.isi)
                }

                this.setOnClickListener {
                    listener.onItemClick(berita)
                }

            }
        }

        private fun getTime(berita: berita, view: View) {
            var date = berita.tanggal!!
            var dateText = DateHelper.findTimeDifference(date.removeRange(date.length-3, date.length), POST)
            view.tanggal.text = " . $dateText"
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
                                .into(newsImg)
                        }

                    }
                }
        }
    }

    fun addData(berita: berita) {
        news.add(berita)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_latest_news, parent, false)
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