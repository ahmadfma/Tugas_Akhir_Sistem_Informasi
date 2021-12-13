package com.example.indonews_app.Model

data class DataCovidProvinsi(
    val current_data: Double,
    val last_date: String,
    val list_data: List<Provinsi>,
    val missing_data: Double,
    val tanpa_provinsi: Int
)

data class Provinsi(
    val doc_count: Double,
    val jenis_kelamin: List<JenisKelamin>,
    val jumlah_dirawat: Int,
    val jumlah_kasus: Int,
    val jumlah_meninggal: Int,
    val jumlah_sembuh: Int,
    val kelompok_umur: List<KelompokUmur>,
    val key: String,
    val lokasi: Lokasi,
    val penambahan: Penambahan
)

data class JenisKelamin(
    val doc_count: Int,
    val key: String
)

data class KelompokUmur(
    val doc_count: Int,
    val key: String,
    val usia: Usia
)

data class Lokasi(
    val lat: Double,
    val lon: Double
)

data class Penambahan(
    val meninggal: Int,
    val positif: Int,
    val sembuh: Int
)

data class Usia(
    val value: Double
)