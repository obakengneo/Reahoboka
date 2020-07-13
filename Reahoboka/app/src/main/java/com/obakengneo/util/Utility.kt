package com.obakengneo.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obakengneo.R
import com.obakengneo.db.SharedPref
import com.obakengneo.model.Hymn
import com.obakengneo.presentation.ActivityDisplayContent
import com.obakengneo.presentation.ActivityMain
import com.obakengneo.presentation.FragmentHymn
import com.obakengneo.presentation.FragmentPrayer
import kotlinx.android.synthetic.main.add_alert_dialog.view.*
import kotlinx.android.synthetic.main.bottom_favourites.view.*
import kotlinx.android.synthetic.main.show_toast.view.*

object Utility {
    fun setTheme(context: Context, activity: Activity) : Boolean {
        val sharedPref = SharedPref(context)

        return if (sharedPref.loadNightModeState() == true) {
            activity.setTheme(R.style.DarkTheme)
            true
        } else {
            activity.setTheme(R.style.AppTheme)
            false
        }
    }

    fun displayToast (message:String, context: Context, length:String, activity: Activity) {
        val toast = Toast(context)
        val toastView: View = activity.layoutInflater.inflate(R.layout.show_toast, null)

        toastView.txtToastMessage.text = message

        when (length) {
            "Short" -> {
                toast.duration = Toast.LENGTH_SHORT
            }
            else -> {
                toast.duration = Toast.LENGTH_LONG
            }
        }
        toast.view = toastView
        toast.show()
    }

    fun getLanguage (activity: Activity) : Int {
        val sharedPreferences: SharedPreferences = activity.getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        var language = 0

        if (sharedPreferences.contains("Language")) {
            language = sharedPreferences.getInt("Language", 0)
        }
        return language
    }

    fun navigateToActivity(activity: Activity, context: Context, classToNavigateTo: Class<*>?) {
        val intent = Intent(context, classToNavigateTo)
            activity.startActivity(intent)
    }

    fun navigateToActivityWithData(name:String, putExtraString:String, activity: Activity, context: Context, classToNavigateTo: Class<*>?) {
        val intent = Intent(context, classToNavigateTo)
        intent.putExtra(name, putExtraString)
        activity.startActivity(intent)
    }

    fun setUpToolBar (activity: Activity, title:String) {
        val actionBar = (activity as AppCompatActivity).supportActionBar

        actionBar!!.title = title
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer)
    }

    fun getColor (color:Int, requiredContext: Context) : Int {
        val typedValue = TypedValue()
        requiredContext.theme?.resolveAttribute(color, typedValue, true)
        return  ContextCompat.getColor(requiredContext, typedValue.resourceId)
    }

    fun getFragment (activity: Activity) : Int {
        if(getSelectedItem(activity) == 1 || getSelectedItem(activity) == 2) {
            return 0
        }
        return 1
    }

    private fun getSelectedItem(activity: Activity): Int {
        val bottomNavigationView = activity.findViewById<BottomNavigationView>(R.id.bottom_nav)
        val menu = bottomNavigationView.menu

        for (i in 0 until bottomNavigationView.menu.size()) {
            val menuItem = menu.getItem(i)

            if (menuItem.isChecked) {
                return i
            }
        }
        return 0
    }

    fun getFilteredLanguage (activity: Activity) : Int {
        val sharedPref: SharedPreferences = activity.getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        var filteredLanguage = 0

        if (sharedPref.contains("FilteredLanguage")) {
            filteredLanguage = sharedPref.getInt("FilteredLanguage", 0)
        }

        return filteredLanguage
    }

    fun navigateToAnotherActivity (activity: Activity, context: Context, id: String, name: String, content: String) {
        val intent = Intent(context, ActivityDisplayContent::class.java)

        intent.putExtra("id", id)
        intent.putExtra("name", name)
        intent.putExtra("content", content)

        activity.startActivity(intent)
    }

    private fun getArray (array:Int, activity: Activity) : MutableList<String> {
        return activity.resources.getStringArray(array).toMutableList()
    }

    fun getNumbersArray (activity: Activity) : MutableList<String>? {
        return when (getLanguage(activity)) {
            0 -> { getArray(R.array.numberSesotho, activity)}
            1 -> { getArray(R.array.numberIsiZulu, activity)}
            2 -> { getArray(R.array.numberSetswana, activity)}
            3 -> { getArray(R.array.numberIsiXhosa, activity)}
            4 -> { getArray(R.array.numberEnglish, activity)}
            else -> null
        }
    }

    fun getMysteriesArray (activity: Activity) : MutableList<String>? {
        return when (getLanguage(activity)) {
            0 -> { getArray(R.array.mysteriesSesotho, activity)}
            1 -> { getArray(R.array.mysteriesIsiZulu, activity)}
            2 -> { getArray(R.array.mysteriesSetswana, activity)}
            3 -> { getArray(R.array.mysteriesIsiXhosa, activity)}
            4 -> { getArray(R.array.mysteriesEnglish, activity)}
            else -> null
        }
    }

    fun getDaysArray (activity: Activity) : MutableList<String>? {
        return when (getLanguage(activity)) {
            0 -> { getArray(R.array.numberSesotho, activity)}
            1 -> { getArray(R.array.numberIsiZulu, activity)}
            2 -> { getArray(R.array.numberSetswana, activity)}
            3 -> { getArray(R.array.numberIsiXhosa, activity)}
            4 -> { getArray(R.array.numberEnglish, activity)}
            else -> null
        }
    }
}