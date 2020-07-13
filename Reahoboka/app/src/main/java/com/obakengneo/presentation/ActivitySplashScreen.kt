package com.obakengneo.presentation

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.backendless.persistence.DataQueryBuilder
import com.obakengneo.*
import com.obakengneo.backendless.BackendUtility
import com.obakengneo.db.DataBaseHandler
import com.obakengneo.model.Mystery
import com.obakengneo.model.Hymn
import com.obakengneo.model.Prayer
import com.obakengneo.util.Utility
import kotlin.system.exitProcess

class ActivitySplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handler: Handler
        val sharedPref: SharedPreferences = getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)

        Utility.setTheme(this, this)
        setContentView(R.layout.activity_splash_screen)

        if(!sharedPref.contains("User")) {
            if (isNetworkConnected()) {
                val dataQuery = DataQueryBuilder.create()
                val dataQueryPrayer = DataQueryBuilder.create()

                BackendUtility.getHymnData(this, dataQuery, 0)
                BackendUtility.getHymnData(this, dataQuery, 1)
                BackendUtility.getHymnData(this, dataQuery, 1)
                BackendUtility.getHymnData(this, dataQuery, 1)
                BackendUtility.getHymnData(this, dataQuery, 1)
                BackendUtility.getHymnData(this, dataQuery, 1)

                BackendUtility.getPrayerData(this, dataQueryPrayer,0)
                BackendUtility.getPrayerData(this, dataQueryPrayer,1)

                BackendUtility.getMysteryData(this)

                handler = Handler()
                handler.postDelayed({
                    isDataInserted()
                }, 5000)

            } else {
                Utility.displayToast("Please connect to a network to set up application", this, "Long", this)
                closeApplication()
            }
        } else {
            handler = Handler()
            handler.postDelayed({

                Utility.navigateToActivity(this, this, ActivityMain::class.java)
            }, 2000)
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    private fun isDataInserted () {
        val prayer : MutableList<Prayer>
        val hymn: MutableList<Hymn>
        val mystery: MutableList<Mystery>
        val db = DataBaseHandler(this)
        val sharedPref: SharedPreferences = getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()

        prayer = db.getPrayerData("")
        hymn = db.getHymnData("")
        mystery = db.getMysteryData("")

        if (prayer.size != 0 && mystery.size != 0 && hymn.size != 0) {
            editor.putString("User", "User")
            editor.apply()

            Utility.displayToast("Set up successful. Welcome and enjoy.",this, "Long",this)
            Utility.navigateToActivity(this, this, ActivityMain::class.java)
        } else {
            editor.remove("User")
            editor.commit()

            Utility.displayToast("Please connect to the internet to set up application." + prayer.size + " " + hymn.size + " " + mystery.size,this, "Long",this)
            closeApplication()
        }
    }

    private fun closeApplication() {
        val handler = Handler()
        handler.postDelayed({
            moveTaskToBack(true);
            exitProcess(-1)
        }, 5000)
    }
}
