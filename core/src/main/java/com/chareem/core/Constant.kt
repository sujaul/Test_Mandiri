package com.chareem.core

import android.Manifest

object Constant {
    val data_pref = "data.pref"
    const val DATABASE_NAME = "data38.db" // nama database
    const val DATABASE_VERSION = 38 // version database
    var APP_ID = "App" // ambil dari manifest app

    var BASE_URL = BuildConfig.BASE_URL
    var URL_DEV = BuildConfig.URL_DEV
    var GOOGLE_API_KEY = BuildConfig.API_KEY
    val page = 20
    val permissions = mutableListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
}