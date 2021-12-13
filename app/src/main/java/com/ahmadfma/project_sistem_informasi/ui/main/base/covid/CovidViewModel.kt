package com.ahmadfma.project_sistem_informasi.ui.main.base.covid

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.indonews_app.Model.*
import com.example.indonews_app.Network.covid.RetrofitClientCovid
import retrofit2.Response
import java.lang.Exception

class CovidViewModel: ViewModel() {
    private val TAG = "CovidViewModel"

    var totalKasus: Total? = null
    var kasusBaru: PenambahanUpdate? = null
    var listDataProvinsi: List<Provinsi>? = null
    var listHarian: List<Harian>? = null

    suspend fun getTotalKasusFromAPI(): Boolean {
        Log.d(TAG, "getTotalKasusFromAPI requesting")
        var response: Response<DataUpdateCovid>? = null
        return try {
            response = RetrofitClientCovid.instance.getCovidUpdate()
            if(response.isSuccessful) {
                totalKasus = response.body()?.update?.total!!
                listHarian = response.body()?.update?.harian!!
                true
            } else {
                Log.e(TAG, "getTotalKasusFromAPI Response Failed, Response Code: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}, getTotalKasusFromAPI Response Failed, Response Code: ${response?.code()}")
            false
        }
    }

    suspend fun getKasusBaruFromAPI(): Boolean {
        Log.d(TAG, "getKasusBaruFromAPI requesting")
        var response: Response<DataUpdateCovid>? = null
        return try {
            response = RetrofitClientCovid.instance.getCovidUpdate()
            if(response.isSuccessful) {
                kasusBaru = response.body()?.update?.penambahan!!
                true
            } else {
                Log.e(TAG, "getKasusBaruFromAPI Response Failed, Response Code: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}, getKasusBaruFromAPI Response Failed, Response Code: ${response?.code()}")
            false
        }
    }

    suspend fun getDataProvinsiFromAPI(): Boolean {
        Log.d(TAG, "getDataProvinsiFromAPI requesting")
        var response: Response<DataCovidProvinsi>? = null
        return try {
            response = RetrofitClientCovid.instance.getCovidProvinsi()
            if(response.isSuccessful) {
                listDataProvinsi = response.body()?.list_data!!
                date_provinsi = response.body()!!.last_date
                true
            } else {
                Log.e(TAG, "getDataProvinsiFromAPI Response Failed, Response Code: ${response.code()} ")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "${e.message}, getDataProvinsiFromAPI Response Failed, Response Code: ${response?.code()} ")
            false
        }

    }

    companion object {
        @JvmStatic
        var date_provinsi = ""
    }

}