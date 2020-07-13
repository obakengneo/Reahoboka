package com.obakengneo.backendless

import android.app.Activity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.obakengneo.db.DataBaseHandler
import com.obakengneo.model.Hymn
import com.obakengneo.model.Mystery
import com.obakengneo.model.Prayer

object BackendUtility {
    fun getMysteryData (activity: Activity) {
        var dbMystery: Mystery
        val db = DataBaseHandler(activity)

        val dataQuery = DataQueryBuilder.create()
        dataQuery.setPageSize(100)

            Backendless.Data.of(Mystery::class.java)
                .find(dataQuery, object : AsyncCallback<MutableList<Mystery>> {
                    var newMystery: MutableList<Mystery> = ArrayList()
                    override fun handleResponse(mystery: MutableList<Mystery>) {
                        do {
                            newMystery = mystery
                        } while (mystery == null)
                        saveMysteriesToDataLocalStorage()
                    }

                    override fun handleFault(error: BackendlessFault) {
                    }

                    fun saveMysteriesToDataLocalStorage() {
                        for (i in newMystery) {
                            dbMystery = Mystery(
                                i.language,
                                i.firstMystery,
                                i.secondMystery,
                                i.thirdMystery,
                                i.fourthMystery,
                                i.fifthMystery,
                                i.mysteryName
                            )
                            db.insertMysteryData(dbMystery)
                        }
                    }
                })
    }

    fun getHymnData (activity: Activity, dataQuery: DataQueryBuilder, count:Int) {
        var dbHymn: Hymn
        val db = DataBaseHandler(activity)

        when (count) {
            0 -> {
                dataQuery.setPageSize(100)
            }
            else -> {
                dataQuery.setPageSize(100)
                dataQuery.prepareNextPage()
            }
        }

        Backendless.Data.of(Hymn::class.java)
            .find(dataQuery, object : AsyncCallback<MutableList<Hymn>> {
                var newHymn: MutableList<Hymn> = ArrayList()
                override fun handleResponse(hymn: MutableList<Hymn>) {
                    do {
                        newHymn = hymn
                    } while (hymn == null)
                    saveHymnToDataLocalStorage()
                }

                override fun handleFault(error: BackendlessFault) {
                }

                fun saveHymnToDataLocalStorage() {
                    for (i in newHymn) {
                        dbHymn =
                            Hymn(i.language, i.hymn, i.id, i.name)
                        db.insertHymnData(dbHymn)
                    }
                }
            })
    }

    fun getPrayerData (activity: Activity, dataQuery: DataQueryBuilder, count:Int) {
        var dbPrayer: Prayer
        val db = DataBaseHandler(activity)

        when (count) {
            0 -> {
                dataQuery.setPageSize(100)
            }
            else -> {
                dataQuery.setPageSize(100)
                dataQuery.prepareNextPage()
            }
        }

        Backendless.Data.of(Prayer::class.java).find(dataQuery, object :
            AsyncCallback<MutableList<Prayer>> {
            var newPrayer: MutableList<Prayer> = ArrayList()
            override fun handleResponse(prayer: MutableList<Prayer>) {
                do {
                    newPrayer = prayer
                } while (prayer == null)

                saveHymnToDataLocalStorage()
            }

            override fun handleFault(error: BackendlessFault) {}

            fun saveHymnToDataLocalStorage() {
                for (i in newPrayer) {
                    dbPrayer =
                        Prayer(i.language, i.prayer, i.id, i.name)
                    db.insertPrayerData(dbPrayer)
                }
            }
        })
    }
}
