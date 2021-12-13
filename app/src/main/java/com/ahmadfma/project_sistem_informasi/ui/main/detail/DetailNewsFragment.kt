package com.ahmadfma.project_sistem_informasi.ui.main.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.LIST_CATEGORY
import com.ahmadfma.project_sistem_informasi.data.berita
import com.ahmadfma.project_sistem_informasi.data.konten
import com.ahmadfma.project_sistem_informasi.utilities.DateHelper
import com.ahmadfma.project_sistem_informasi.utilities.POST
import com.bumptech.glide.Glide
import com.example.indonews_app.UserData.UserData_Berita
import com.example.indonews_app.UserData.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_detail_news.*
import kotlinx.android.synthetic.main.item_latest_news.view.*
import kotlinx.android.synthetic.main.item_list_news.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailNewsFragment : Fragment() {

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        return inflater.inflate(R.layout.fragment_detail_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isSaved()

        getCategoryColor(selectedBerita!!, view)
        categoryTxtDetail.text = selectedBerita!!.kategori
        getImage(selectedBerita!!, view)
        titleTv.text = selectedBerita!!.judul
        getStringFromHtml(titleTv, selectedBerita!!.judul!!)
        getStringFromHtml(author, selectedBerita!!.author!!)
        getStringFromHtml(source, selectedBerita!!.penerbit!!)
        getStringFromHtml(contentText, selectedBerita!!.isi!!)
        publistv.text = DateHelper.findTimeDifference(selectedBerita!!.tanggal!!, POST)

        bookmarkBtn.setOnClickListener {
            if(bookmarkBtn.tag == "saved") {
                deleteBerita()
            }
            else {
                saveBerita()
            }
        }

        backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        selengkapnyaBtn.setOnClickListener {
            val url = selectedBerita!!.url_berita
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        shareBtn.setOnClickListener {
            shareAction()
        }

        addViewer()

    }

    private fun isSaved() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                userViewModel.readAllData.observe(viewLifecycleOwner, Observer {
                    for(id in it) {
                        if(id.berita_id == selectedBerita!!.id!!) {
                            iconBookmarkBtn.setImageResource(R.drawable.ic_bookmark_pressed)
                            bookmarkBtn.tag = "saved"
                            return@Observer
                        }
                    }
                })
            }
        }
    }

    private fun saveBerita() {
        userViewModel.addArticle(UserData_Berita(0, selectedBerita!!.id!!))
        iconBookmarkBtn.setImageResource(R.drawable.ic_bookmark_pressed)
        bookmarkBtn.tag = "saved"
    }

    private fun deleteBerita() {
        userViewModel.deleteSpecificArticleFromDatabase(selectedBerita!!.id!!)
        iconBookmarkBtn.setImageResource(R.drawable.ic_bookmark)
        bookmarkBtn.tag = "unsaved"
    }

    private fun getStringFromHtml(view: TextView, txt: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.text = Html.fromHtml(txt, Html.FROM_HTML_MODE_LEGACY)
        } else {
            view.text = Html.fromHtml(txt)
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
                if (it.isSuccessful) {
                    for (doc in it.result!!) {
                        images.add(doc.toObject(konten::class.java))
                    }
                    with(view) {
                        Glide.with(context)
                            .load(images[0].url)
                            .into(imgSelect)
                    }

                }
            }
    }

    private fun getCategoryColor(berita: berita, view: View) {
        with(view) {
            when(berita.kategori) {
                LIST_CATEGORY[1] -> setColor(categoryBackgroundDetail, R.color.Bisnis)
                LIST_CATEGORY[2] -> setColor(categoryBackgroundDetail, R.color.Science)
                LIST_CATEGORY[3] -> setColor(categoryBackgroundDetail, R.color.Sport)
                LIST_CATEGORY[4] -> setColor(categoryBackgroundDetail, R.color.Otomotif)
                LIST_CATEGORY[5] -> setColor(categoryBackgroundDetail, R.color.Edukasi)
                LIST_CATEGORY[6] -> setColor(categoryBackgroundDetail, R.color.Travel)
                LIST_CATEGORY[7] -> setColor(categoryBackgroundDetail, R.color.Health)
                LIST_CATEGORY[8] -> setColor(categoryBackgroundDetail, R.color.Lifestyle)
                LIST_CATEGORY[9] -> setColor(categoryBackgroundDetail, R.color.Teknologi)
            }
        }
    }

    private fun setColor(layout: View, colorId: Int) {
        layout.background.setTint(layout.resources.getColor(colorId));
    }

    private fun shareAction() {

        val text = "${selectedBerita!!.judul} - Baca selengkapnya di : ${selectedBerita!!.url_berita}"

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun addViewer() {
        if(selectedBerita!!.viewer != null) {
            var i = selectedBerita!!.viewer!!.toInt() + 1
            Firebase.firestore
                .collection("berita")
                .document(selectedBerita!!.id!!)
                .update("viewer",""+i)
        } else {
            Firebase.firestore
                .collection("berita")
                .document(selectedBerita!!.id!!)
                .update("viewer","1")
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) = DetailNewsFragment()
        var selectedBerita: berita? = null
    }
}

