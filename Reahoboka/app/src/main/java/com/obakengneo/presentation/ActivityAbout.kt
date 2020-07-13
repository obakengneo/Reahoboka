package com.obakengneo.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.obakengneo.R
import com.obakengneo.util.Utility

class ActivityAbout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utility.setTheme(this, this)
        setContentView(R.layout.activity_about)
        setUpToolBar()

        val scripture = findViewById<TextView>(R.id.txtAbout)
        scripture.movementMethod = ScrollingMovementMethod()
    }

    override fun onSupportNavigateUp(): Boolean {
        Utility.navigateToActivity(this,this, ActivityMain::class.java)
        return true
    }

    override fun onBackPressed() {
        Utility.navigateToActivity(this,this, ActivityMain::class.java)
    }

    private fun setUpToolBar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarAbout)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        actionBar!!.setCustomView(R.layout.action_bar)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.back_button)

        val txtActionBarTitle: TextView = findViewById<TextView>(R.id.tvTitle)
        val title = "About"
        txtActionBarTitle.text = title
    }
}
