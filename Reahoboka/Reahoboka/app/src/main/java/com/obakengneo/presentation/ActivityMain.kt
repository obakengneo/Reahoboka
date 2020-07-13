package com.obakengneo.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import com.obakengneo.R
import com.obakengneo.db.DataBaseHandler
import com.obakengneo.util.Utility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_language_sheet.view.*

class ActivityMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Utility.setTheme(this, this)
        setContentView(R.layout.activity_main)
        setUpToolBar()
        setUpBottomNavigationView()

        val drawLayout = findViewById<DrawerLayout>(R.id.drawer)
        val toggle = ActionBarDrawerToggle(this, drawLayout, toolbar, R.string.drawerOpen, R.string.drawerClose)
        drawLayout.addDrawerListener(toggle)
        toggle.syncState()

        findViewById<NavigationView>(R.id.navigationView).setNavigationItemSelectedListener(this)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setUpBottomNavBar(navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> { Utility.navigateToActivity(this, this, ActivitySettings::class.java)}
            R.id.share -> { intentHandler("Share")}
            R.id.rate -> { intentHandler("Rate")}
            R.id.update -> { updateApplicationData()}
            R.id.language -> { showBottomNavigationBar()}
        }
        return true
    }

    private fun setUpBottomNavigationView () {
        val rosary = this.resources.getStringArray(R.array.rosary).toMutableList()
        val hymns = this.resources.getStringArray(R.array.hymn).toMutableList()
        val prayers = this.resources.getStringArray(R.array.prayer).toMutableList()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNavigationView.menu.getItem(1).title = hymns[Utility.getLanguage(this)]
        bottomNavigationView.menu.getItem(2).title = prayers[Utility.getLanguage(this)]
        bottomNavigationView.menu.getItem(3).title = rosary[Utility.getLanguage(this)]
    }

    private fun setUpToolBar () {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar!!.setCustomView(R.layout.action_bar)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setUpBottomNavBar(navController: NavController) {
        bottom_nav.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun intentHandler (navigateTo:String) {
        when(navigateTo) {
            "Rate" -> {
                val browse = Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.obakengneo"))
                startActivity(browse)

                Utility.displayToast("I would highly appreciate a 5 star rating. Thank you in advance.", this, "Long", this)
            }
            "Share" -> {
                val intentShare = Intent()
                intentShare.action = Intent.ACTION_SEND
                intentShare.putExtra(Intent.EXTRA_TEXT, "Get Binang le Rapele App\r\n http://play.google.com/store/apps/details?id=com.obakengneo")
                intentShare.type = "text/plain"

                startActivity(Intent.createChooser(intentShare, "Share link via:"))
            }
        }
    }

    private fun updateApplicationData () {
        val view: View = layoutInflater.inflate(R.layout.refresh_alert_dialog, null)
        val btnNo = view.findViewById<Button>(R.id.btnNo)
        val btnContinue = view.findViewById<Button>(R.id.btnContinue)
        val alertDialog: AlertDialog = AlertDialog.Builder(this).setView(view).create()
        val sharedPref: SharedPreferences = getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        val db = DataBaseHandler(this)
        val handler = Handler()

        alertDialog.show()

        btnNo.setOnClickListener { alertDialog.dismiss() }
        btnContinue.setOnClickListener{ alertDialog.dismiss()
            editor.remove("User")
            editor.apply()
            db.deleteTable()

            handler.postDelayed({
                Utility.navigateToActivity(this, this, ActivitySplashScreen::class.java)
                finish()
            }, 1000)
        }
    }

    private fun showBottomNavigationBar () {
        val view: View = layoutInflater.inflate(R.layout.bottom_language_sheet, null)
        val bottomSheetDialog = BottomSheetDialog(this)

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        view.languageSesotho.setOnClickListener {
            setLanguage(0)
            Utility.displayToast("Language changed to Sesotho", this, "Short", this)
            Utility.navigateToActivity(this,this, ActivityMain::class.java)
            bottomSheetDialog.dismiss()
        }

        view.languageIsiZulu.setOnClickListener {
            setLanguage(1)
            Utility.displayToast("Language changed to IsiZulu", this, "Short", this)
            Utility.navigateToActivity(this,this, ActivityMain::class.java)
            bottomSheetDialog.dismiss()
        }

        view.languageSetswana.setOnClickListener {
            setLanguage(2)
            Utility.displayToast("Language changed to Setswana", this, "Short", this)
            Utility.navigateToActivity(this,this, ActivityMain::class.java)
            bottomSheetDialog.dismiss()
        }

        view.languageXhosa.setOnClickListener {
            setLanguage(3)
            Utility.displayToast("Language changed to IsiXhosa", this, "Short", this)
            Utility.navigateToActivity(this,this, ActivityMain::class.java)
            bottomSheetDialog.dismiss()
        }

        view.languageEnglish.setOnClickListener {
            setLanguage(4)
            Utility.displayToast("Language changed to English", this, "Short", this)
            Utility.navigateToActivity(this,this, ActivityMain::class.java)
            bottomSheetDialog.dismiss()
        }
    }

    private fun setLanguage (language: Int) {
        val sharedPref: SharedPreferences = getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.putInt("Language", language)
        editor.apply()
    }
}
