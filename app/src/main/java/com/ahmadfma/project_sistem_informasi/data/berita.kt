package com.ahmadfma.project_sistem_informasi.data

data class berita(
    var id: String? = null,
    var judul: String? = null,
    var isi: String? = null,
    var kategori: String? = null,
    var tanggal: String? = null,
    var author: String? = null,
    var penerbit: String? = null,
    var url_berita: String? = null,
    var viewer: String? = null,
)