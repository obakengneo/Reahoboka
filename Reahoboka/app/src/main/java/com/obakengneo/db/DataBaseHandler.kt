package com.obakengneo.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.obakengneo.model.Mystery
import com.obakengneo.model.Hymn
import com.obakengneo.model.Prayer

const val DATABASE_NAME = "MyDB"
const val TABLE_NAME = "Hymn"
const val COL_NAME = "name"
const val COL_HYMN = "hymn"
const val COL_ID = "id"
const val COL_LANGUAGE = "language"

const val TABLE_NAME_PRAYER = "Prayer"
const val COL_ID_PRAYER = "id"
const val COL_NAME_PRAYER = "name"
const val COL_PRAYER = "prayer"
const val COL_LANGUAGE_PRAYER = "language"

const val TABLE_NAME_MYSTERY = "Mystery"
const val COL_NAME_MYSTERY = "name"
const val COL_MYSTERY_1 = "first_mystery"
const val COL_MYSTERY_2 = "second_mystery"
const val COL_MYSTERY_3 = "third_mystery"
const val COL_MYSTERY_4 = "fourth_mystery"
const val COL_MYSTERY_5 = "fifth_mystery"
const val COL_LANGUAGE_MYSTERY = "language"

class DataBaseHandler (context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ($COL_LANGUAGE VARCHAR(50),$COL_ID INTEGER,$COL_NAME VARCHAR(256),$COL_HYMN VARCHAR(500))"

        val createTablePrayer=
            "CREATE TABLE $TABLE_NAME_PRAYER ($COL_LANGUAGE_PRAYER VARCHAR(50),$COL_ID_PRAYER INTEGER,$COL_NAME_PRAYER VARCHAR(256),$COL_PRAYER VARCHAR(1000))"

        val createTableMystery =
            "CREATE TABLE $TABLE_NAME_MYSTERY ($COL_LANGUAGE_MYSTERY VARCHAR(50),$COL_NAME_MYSTERY VARCHAR(1000),$COL_MYSTERY_1 VARCHAR(1000),$COL_MYSTERY_2 VARCHAR(1000),$COL_MYSTERY_3 VARCHAR(1000),$COL_MYSTERY_4 VARCHAR(1000),$COL_MYSTERY_5 VARCHAR(1000))"

        db?.execSQL(createTable); db?.execSQL(createTablePrayer); db?.execSQL(createTableMystery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val createTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        val createTablePrayer = "DROP TABLE IF EXISTS $TABLE_NAME_PRAYER"
        val createTableMystery = "DROP TABLE IF EXISTS $TABLE_NAME_MYSTERY"

        db?.execSQL(createTable); db?.execSQL(createTablePrayer); db?.execSQL(createTableMystery)
        onCreate(db)
    }

    fun insertHymnData(hymn: Hymn) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_LANGUAGE, hymn.language)
        cv.put(COL_ID, hymn.id)
        cv.put(COL_NAME, hymn.name)
        cv.put(COL_HYMN, hymn.hymn)

        db.insert(TABLE_NAME, null, cv)
        db.close()
    }

    fun insertPrayerData(prayer: Prayer) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_LANGUAGE_PRAYER, prayer.language)
        cv.put(COL_ID_PRAYER, prayer.id)
        cv.put(COL_NAME_PRAYER, prayer.name)
        cv.put(COL_PRAYER, prayer.prayer)

        db.insert(TABLE_NAME_PRAYER, null, cv)
        db.close()
    }

    fun insertMysteryData(mystery: Mystery) {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_LANGUAGE_MYSTERY, mystery.language)
        cv.put(COL_NAME_MYSTERY, mystery.mysteryName)
        cv.put(COL_MYSTERY_1, mystery.firstMystery)
        cv.put(COL_MYSTERY_2, mystery.secondMystery)
        cv.put(COL_MYSTERY_3, mystery.thirdMystery)
        cv.put(COL_MYSTERY_4, mystery.fourthMystery)
        cv.put(COL_MYSTERY_5, mystery.fifthMystery)

        db.insert(TABLE_NAME_MYSTERY, null, cv)
        db.close()
    }

    fun getHymnData (str:String) : MutableList<Hymn> {
        val list : MutableList<Hymn> = ArrayList()

        val db = this.readableDatabase

        val query:String = if (str == "") {
            "Select * from $TABLE_NAME order by $COL_ID asc"
        } else {
            "Select * from $TABLE_NAME where $COL_LANGUAGE ='$str' order by $COL_ID asc"
        }

        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val hymn = Hymn()
                hymn.language = result.getString(result.getColumnIndex(COL_LANGUAGE))
                hymn.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                hymn.name = result.getString(result.getColumnIndex(COL_NAME))
                hymn.hymn = result.getString(result.getColumnIndex(COL_HYMN))

                list.add(hymn)
            } while (result.moveToNext())
        }
        result.close()
        db.close()

        return list
    }

    fun getPrayerData (str:String) : MutableList<Prayer>{
        val list : MutableList<Prayer> = ArrayList()
        val db = this.readableDatabase

        val query:String = if (str == "") {
            "Select * from $TABLE_NAME_PRAYER order by $COL_ID_PRAYER asc"
        } else {
            "Select * from $TABLE_NAME_PRAYER where $COL_LANGUAGE_PRAYER ='$str' order by $COL_ID_PRAYER asc"
        }

        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val prayer = Prayer()
                prayer.language = result.getString(result.getColumnIndex(COL_LANGUAGE_PRAYER))
                prayer.id = result.getString(result.getColumnIndex(COL_ID_PRAYER)).toInt()
                prayer.name = result.getString(result.getColumnIndex(COL_NAME_PRAYER))
                prayer.prayer = result.getString(result.getColumnIndex(COL_PRAYER))

                list.add(prayer)
            } while (result.moveToNext())
        }
        result.close()
        db.close()

        return list
    }

    fun getMysteryData (str:String) : MutableList<Mystery>{
        val list : MutableList<Mystery> = ArrayList()
        val db = this.readableDatabase

        val query:String = if (str == "") {
            "Select * from $TABLE_NAME_MYSTERY"
        } else {
            "Select * from $TABLE_NAME_MYSTERY where $COL_LANGUAGE_MYSTERY ='$str'"
        }

        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val mystery = Mystery()
                mystery.language = result.getString(result.getColumnIndex(COL_LANGUAGE_MYSTERY))
                mystery.mysteryName = result.getString(result.getColumnIndex(COL_NAME_MYSTERY))
                mystery.firstMystery = result.getString(result.getColumnIndex(COL_MYSTERY_1))
                mystery.secondMystery = result.getString(result.getColumnIndex(COL_MYSTERY_2))
                mystery.thirdMystery = result.getString(result.getColumnIndex(COL_MYSTERY_3))
                mystery.fourthMystery = result.getString(result.getColumnIndex(COL_MYSTERY_4))
                mystery.fifthMystery = result.getString(result.getColumnIndex(COL_MYSTERY_5))

                list.add(mystery)
            } while (result.moveToNext())
        }
        result.close()
        db.close()

        return list
    }

    fun deleteTable () {
        val db = this.readableDatabase
        db.execSQL("delete from $TABLE_NAME")
        db.execSQL("delete from $TABLE_NAME_PRAYER")
        db.execSQL("delete from $TABLE_NAME_MYSTERY")

        db.close()
    }
}