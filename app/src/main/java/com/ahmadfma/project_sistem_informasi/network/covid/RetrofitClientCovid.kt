package com.example.indonews_app.Network.covid

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientCovid {

    private const val Base_URL = "https://data.covid19.go.id/"

    val instance : CovidEndPoint by lazy {
        val retrofit = Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(CovidEndPoint::class.java)
    }

}