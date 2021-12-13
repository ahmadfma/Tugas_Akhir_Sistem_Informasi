package com.ahmadfma.project_sistem_informasi.ui.main.base.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.data.LIST_CATEGORY
import com.ahmadfma.project_sistem_informasi.ui.main.base.category.CategoriedNewsFragment
import kotlinx.android.synthetic.main.item_kategori_btn.view.*

class KategoriAdapter(private val fragment: Fragment): RecyclerView.Adapter<KategoriAdapter.Holder>() {

    var selectedIndex = 0

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(kategori: String) {
            with(itemView) {
                kategoriText.text = kategori

                if(selectedIndex == bindingAdapterPosition) {
                    kategoriBtn.setCardBackgroundColor(resources.getColor(R.color.hero))
                } else {
                    kategoriBtn.setCardBackgroundColor(resources.getColor(R.color.c3))
                }

                kategoriBtn.setOnClickListener {
                    CategoriedNewsFragment.selectedCategory = kategori
                    fragment.findNavController().navigate(R.id.action_baseFragment_to_categoriedNewsFragment)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori_btn, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(LIST_CATEGORY[position])
    }

    override fun getItemCount(): Int {
        return LIST_CATEGORY.size
    }

}