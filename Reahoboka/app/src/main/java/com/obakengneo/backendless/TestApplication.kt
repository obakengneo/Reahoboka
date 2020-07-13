package com.obakengneo.backendless

import android.app.Application
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.obakengneo.db.DataBaseHandler
import com.obakengneo.model.Hymn
import com.obakengneo.model.Mystery
import com.obakengneo.model.Prayer


class TestApplication : Application() {
    val APPLICATION_ID = "4CA4AA12-E51C-AB84-FF92-7CFF8D2F7300"
    val API_KEY = "09207A85-2C0E-4040-BA69-2503BBA8A61B"
    val SERVER_URL = "http://api.backendless.com"

    override fun onCreate() {
        super.onCreate()

        Backendless.setUrl(SERVER_URL)
        Backendless.initApp(applicationContext, APPLICATION_ID, API_KEY)
    }
}