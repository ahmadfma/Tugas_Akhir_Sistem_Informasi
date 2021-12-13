package com.ahmadfma.project_sistem_informasi.ui.main.base.covid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmadfma.project_sistem_informasi.R
import com.example.indonews_app.Model.Provinsi
import kotlinx.android.synthetic.main.item_list_provinsi.view.*

class ProvinsiAdapter(private val listProvinsi : MutableList<Provinsi>, private val listener: ProvinsiAdapter.Listener): RecyclerView.Adapter<ProvinsiAdapter.Holder>() {

    interface Listener {
        fun onItemClick(provinsi: Provinsi)
    }

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(provinsi: Provinsi, listener: Listener) {
            with(itemView) {
                namaProvinsiTxt.text = provinsi.key
                positifTxt.text = ""+provinsi.jumlah_kasus
                meninggalTxt.text = ""+provinsi.jumlah_meninggal
                sembuhtxt.text = ""+provinsi.jumlah_sembuh
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_provinsi, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listProvinsi[position], listener)
    }

    override fun getItemCount(): Int {
        return listProvinsi.size
    }


}