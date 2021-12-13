package com.example.indonews_app.Model

data class DataUpdateCovid(
    val `data`: DataUpdate,
    val update: Update
)

data class DataUpdate(
    val id: Int,
    val jumlah_odp: Int,
    val jumlah_pdp: Int,
    val total_spesimen: Int,
    val total_spesimen_negatif: Int
)

data class Update(
    val harian: List<Harian>,
    val penambahan: PenambahanUpdate,
    val total: Total
)

data class Harian(
    val doc_count: Int,
    val jumlah_dirawat: JumlahDirawat,
    val jumlah_dirawat_kum: JumlahDirawatKum,
    val jumlah_meninggal: JumlahMeninggal,
    val jumlah_meninggal_kum: JumlahMeninggalKum,
    val jumlah_positif: JumlahPositif,
    val jumlah_positif_kum: JumlahPositifKum,
    val jumlah_sembuh: JumlahSembuh,
    val jumlah_sembuh_kum: JumlahSembuhKum,
    val key: Long,
    val key_as_string: String
)

data class PenambahanUpdate(
    val created: String,
    val jumlah_dirawat: Int,
    val jumlah_meninggal: Int,
    val jumlah_positif: Int,
    val jumlah_sembuh: Int,
    val tanggal: String
)

data class Total(
    val jumlah_dirawat: Int,
    val jumlah_meninggal: Int,
    val jumlah_positif: Int,
    val jumlah_sembuh: Int
)

data class JumlahDirawat(
    val value: Int
)

data class JumlahDirawatKum(
    val value: Int
)

data class JumlahMeninggal(
    val value: Int
)

data class JumlahMeninggalKum(
    val value: Int
)

data class JumlahPositif(
    val value: Int
)

data class JumlahPositifKum(
    val value: Int
)

data class JumlahSembuh(
    val value: Int
)

data class JumlahSembuhKum(
    val value: Int
)