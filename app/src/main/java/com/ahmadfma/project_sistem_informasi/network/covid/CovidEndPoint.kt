package com.example.indonews_app.Network.covid

import com.example.indonews_app.Model.DataCovidProvinsi
import com.example.indonews_app.Model.DataUpdateCovid
import retrofit2.Response
import retrofit2.http.GET

interface CovidEndPoint {

    @GET("public/api/prov.json")
    suspend fun getCovidProvinsi(): Response<DataCovidProvinsi>

    @GET("public/api/update.json")
    suspend fun getCovidUpdate(): Response<DataUpdateCovid>

}