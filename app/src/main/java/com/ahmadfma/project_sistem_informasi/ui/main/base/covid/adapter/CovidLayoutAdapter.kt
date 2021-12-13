package com.ahmadfma.project_sistem_informasi.ui.main.base.covid.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ahmadfma.project_sistem_informasi.R
import com.ahmadfma.project_sistem_informasi.ui.main.base.covid.CovidFragment
import com.ahmadfma.project_sistem_informasi.ui.main.base.covid.CovidViewModel
import com.example.indonews_app.Model.Provinsi
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.item_layout_covid_header.view.*
import kotlinx.android.synthetic.main.item_layout_covid_kasus_baru.view.*
import kotlinx.android.synthetic.main.item_layout_covid_provinsi.view.*
import kotlinx.android.synthetic.main.item_layout_covid_total_kasus.view.*

const val COVID_HEADER = 0
const val TOTAL_KASUS = 1
const val KASUS_BARU = 2
const val KASUS_PROVINSI = 3

val covid_layouts = mutableListOf(
    COVID_HEADER,
    TOTAL_KASUS,
    KASUS_BARU,
    KASUS_PROVINSI
)

class CovidLayoutAdapter(private val fragment: CovidFragment, private val covidViewModel: CovidViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = "CovidLayoutAdapter"

    inner class HeaderHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                setPieChart(this)
            }
        }

        private fun setPieChart(view: View) {
            with(view) {
                pieChart.isDrawHoleEnabled = true
                pieChart.setUsePercentValues(true)
                pieChart.setDrawEntryLabels(false)
                pieChart.description.isEnabled = false
                pieChart.legend.isEnabled = false
                pieChart.setHoleColor(R.color.black)
//                pieChart.setBackgroundColor(resources.getColor(R.color.hero))
                pieChart.holeRadius = 70.0F

                val totalKasus = covidViewModel.totalKasus!!.jumlah_positif
                val totalSembuh = covidViewModel.totalKasus!!.jumlah_sembuh
                val totalMeninggal = covidViewModel.totalKasus!!.jumlah_meninggal
                val totalDirawat = covidViewModel.totalKasus!!.jumlah_dirawat

                val colors = arrayListOf<Int>(
                    ContextCompat.getColor(fragment.requireContext(), R.color.orange),
                    ContextCompat.getColor(fragment.requireContext(), R.color.green),
                    ContextCompat.getColor(fragment.requireContext(), R.color.red)
                )
                val listEntry: ArrayList<PieEntry> = arrayListOf()

                val totalAll = totalKasus + totalMeninggal + totalSembuh
                val totalKasusInPercent = totalKasus.toFloat() / totalAll.toFloat() * 100
                val totalSembuhInPercent = totalSembuh.toFloat() / totalAll.toFloat() * 100
                val totalMeninggalInPercent = totalMeninggal.toFloat() / totalAll.toFloat() * 100

                Log.d(TAG, "setPieChart: total kasus : $totalKasus .... ${"%.1f".format(totalKasusInPercent)}")
                Log.d(TAG, "setPieChart: total sembuh : $totalSembuh .... ${"%.1f".format(totalSembuhInPercent)}")
                Log.d(TAG, "setPieChart: total meninggal : $totalMeninggal .... ${"%.1f".format(totalMeninggalInPercent)}%")
                terkonfirmasiPie.text = "${"%.1f".format(totalKasusInPercent)}%"
                sembuhPie.text = "${"%.1f".format(totalSembuhInPercent)}%"
                meninggalPie.text = "${"%.1f".format(totalMeninggalInPercent)}%"

                listEntry.add(PieEntry(totalKasusInPercent, "Total Kasus"))
                listEntry.add(PieEntry(totalSembuhInPercent, "Total Sembuh"))
                listEntry.add(PieEntry(totalMeninggalInPercent, "Total Meninggal"))

                val dataset = PieDataSet(listEntry, "Total")
                dataset.colors = colors
                dataset.sliceSpace = 3f

                val data = PieData(dataset)
                data.setDrawValues(false)
//                data.setValueFormatter(PercentFormatter(pieChart))
//                data.setValueTextSize(10f)
//                data.setValueTextColor(Color.BLACK)
                pieChart.data = data
                pieChart.invalidate()
                pieChart.animateY(1500, Easing.EaseInOutQuad)

            }

        }

    }

    inner class TotalKasusHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                terkonfirmasiCount.text = covidViewModel.totalKasus!!.jumlah_positif.toString()
                sembuhCount.text = covidViewModel.totalKasus!!.jumlah_sembuh.toString()
                dirawatCount.text = covidViewModel.totalKasus!!.jumlah_dirawat.toString()
                meninggalCount.text = covidViewModel.totalKasus!!.jumlah_meninggal.toString()
            }
        }
    }

    inner class KasusBaruHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {
                kasusBaruPositif.text = covidViewModel.kasusBaru!!.jumlah_positif.toString()
                kasusBaruDirawat.text = covidViewModel.kasusBaru!!.jumlah_dirawat.toString()
                kasusBaruSembuh.text = covidViewModel.kasusBaru!!.jumlah_sembuh.toString()
                kasusBaruMeninggal.text = covidViewModel.kasusBaru!!.jumlah_meninggal.toString()
                kasusBaruDate.text = "Tanggal " + covidViewModel.kasusBaru!!.tanggal
            }
        }
    }

    inner class KasusProvinsiHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            with(itemView) {

                val adapter = ProvinsiAdapter(covidViewModel.listDataProvinsi as MutableList<Provinsi>, object : ProvinsiAdapter.Listener {
                    override fun onItemClick(provinsi: Provinsi) {

                    }
                })

                rcView_list_provinsi.setHasFixedSize(true)
                rcView_list_provinsi.layoutManager = LinearLayoutManager(fragment.requireContext())
                rcView_list_provinsi.adapter = adapter
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position) {
            0 -> {
                COVID_HEADER
            }
            1 -> {
                TOTAL_KASUS
            }
            2 -> {
                KASUS_BARU
            }
            3 -> {
                KASUS_PROVINSI
            }
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            COVID_HEADER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_covid_header, parent, false)
                HeaderHolder(view)
            }
            TOTAL_KASUS -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_covid_total_kasus, parent, false)
                TotalKasusHolder(view)
            }
            KASUS_BARU -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_covid_kasus_baru, parent, false)
                KasusBaruHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_covid_provinsi, parent, false)
                KasusProvinsiHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            COVID_HEADER -> {
                (holder as HeaderHolder).bind()
            }
            TOTAL_KASUS -> {
                (holder as TotalKasusHolder).bind()
            }
            KASUS_BARU -> {
                (holder as KasusBaruHolder).bind()
            }
            KASUS_PROVINSI -> {
                (holder as KasusProvinsiHolder).bind()
            }
        }
    }

    override fun getItemCount(): Int {
        return covid_layouts.size
    }


}