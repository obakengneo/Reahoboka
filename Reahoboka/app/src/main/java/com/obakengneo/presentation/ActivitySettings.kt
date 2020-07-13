package com.obakengneo.presentation

import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.obakengneo.R
import com.obakengneo.db.SharedPref
import com.obakengneo.util.Utility

class ActivitySettings: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mode = Utility.setTheme(this, this)
        setContentView(R.layout.settings)
        setUpToolBar()

        val sharedPref = SharedPref(this)
        val enableDarkSwitch = findViewById<View>(R.id.enableDark) as Switch?

        enableDarkSwitch!!.isChecked = mode
        enableDarkSwitch.setOnCheckedChangeListener { _, isChecked ->
            when {
                isChecked -> {
                    sharedPref.setNightModeState(true)
                    restartApp()
                }
                else -> {
                    sharedPref.setNightModeState(false)
                    restartApp()
                }
            }
        }
    }

    private fun setUpToolBar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.settings)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        actionBar!!.setCustomView(R.layout.action_bar)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.back_button)

        val txtActionBarTitle: TextView = findViewById<TextView>(R.id.tvTitle)
        val title = "Settings"
        txtActionBarTitle.text = title
    }

    private fun restartApp() {
        Utility.navigateToActivity(this, this, ActivityMain::class.java)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        Utility.navigateToActivity(this, this, ActivityMain::class.java)
        return true
    }

    override fun onBackPressed() {
        Utility.navigateToActivity(this, this, ActivityMain::class.java)
    }
}