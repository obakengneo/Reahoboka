package com.obakengneo.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obakengneo.R
import com.obakengneo.util.Utility
import kotlinx.android.synthetic.main.add_alert_dialog.view.*

class ActivityDisplayContent : AppCompatActivity() {
    var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utility.setTheme(this, this)
        setContentView(R.layout.activity_display_thapelo)
        setUpToolBar(intent.getStringExtra("name")!!)

        val txtDisplayPrayer = findViewById<TextView>(R.id.txtDisplayPrayer)
        txtDisplayPrayer.movementMethod = ScrollingMovementMethod()
        txtDisplayPrayer.text = intent.getStringExtra("content")!!.trim()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val sharedPref: SharedPreferences = getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val inflater = menuInflater
        val name  = intent.getStringExtra("name")

        inflater.inflate(R.menu.menu_home, menu)

        if(sharedPref.contains(name!!+"Int")) {
            if (sharedPref.getBoolean(name + "Int", true)) {
                menu!!.findItem(R.id.favourites).setIcon(R.drawable.ic_heart_filled)
                num = 1
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.share) {
            shareHandler()
        }

        if (item?.itemId == R.id.favourites) {
            favouritesHandler(item)
        }
        return super.onOptionsItemSelected(item!!)
    }

    private fun setUpToolBar(name:String) {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbarDithapelo)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        actionBar!!.setCustomView(R.layout.action_bar_show_content)
        actionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.back_button)

        val txtActionBarTitle: TextView = findViewById<TextView>(R.id.txtTitle)
        txtActionBarTitle.text = name
    }

    private fun shareHandler () {
        val view: View = layoutInflater.inflate(R.layout.add_alert_dialog, null)
        val name  = intent.getStringExtra("name")
        val content  = intent.getStringExtra("content")
        val bottomSheetDialog = BottomSheetDialog(this)
        val intent = Intent()

        bottomSheetDialog.setContentView(view)
        view.findViewById<LinearLayout>(R.id.layoutAddToFavourites).visibility = View.GONE
        bottomSheetDialog.show()

        view.shareTo.setOnClickListener {
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, name+"\r\n\r\n"+ content)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Share to:"))
            bottomSheetDialog.dismiss()
        }
    }

    private fun favouritesHandler (item: MenuItem) {
        val sharedPref: SharedPreferences = getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        val name  = intent.getStringExtra("name")

        when (num) {
            0 -> {
                item.setIcon(R.drawable.ic_heart_filled)
                editor.putString(name, name)
                editor.putBoolean(name!!+"Int", true)
                editor.apply()

                Utility.displayToast("'${name}' added to favourites list", this, "Short", this)
                num = 1
            }
            else -> {
                item.setIcon(R.drawable.ic_heart_unfilled)
                editor.remove(name)
                editor.putBoolean(name!!+"Int", false)
                editor.apply()

                Utility.displayToast("'${name}' removed from favourites list", this, "Short", this)
                num = 0
            }
        }
    }
}
