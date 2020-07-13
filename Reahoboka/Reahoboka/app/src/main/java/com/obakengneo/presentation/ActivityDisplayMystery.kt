package com.obakengneo.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.method.ScrollingMovementMethod
import android.text.style.UnderlineSpan
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obakengneo.*
import com.obakengneo.db.DataBaseHandler
import com.obakengneo.model.Mystery
import com.obakengneo.util.Utility
import kotlinx.android.synthetic.main.bottom_sheet_layout_dimisteri.view.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ActivityDisplayMystery : AppCompatActivity() {

    lateinit var textToShare: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val number = Utility.getNumbersArray(this)
        Utility.setTheme(this, this)

        setContentView(R.layout.mysteries)
        setUpToolBar()
        populateMystery(number!![0])

        val txtHeading = findViewById<TextView>(R.id.txtNameMisteri)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout_dimisteri, null)
        val bottomSheetDialog = BottomSheetDialog(this)

        view.findViewById<TextView>(R.id.mysteryOne).text = number[0]
        view.findViewById<TextView>(R.id.mysteryTwo).text = number[1]
        view.findViewById<TextView>(R.id.mysteryThree).text = number[2]
        view.findViewById<TextView>(R.id.mysteryFour).text = number[3]
        view.findViewById<TextView>(R.id.mysteryFive).text = number[4]

        txtHeading.setOnClickListener {
            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }

        view.misterPele.setOnClickListener {
            populateMystery(number[0])
            bottomSheetDialog.dismiss()
        }

        view.misteriBobedi.setOnClickListener {
            populateMystery(number[1])
            bottomSheetDialog.dismiss()
        }

        view.misteriBoraro.setOnClickListener {
            populateMystery(number[2])
            bottomSheetDialog.dismiss()
        }

        view.misteriBone.setOnClickListener {
            populateMystery(number[3])
            bottomSheetDialog.dismiss()
        }

        view.misteriBohlano.setOnClickListener {
            populateMystery(number[4])
            bottomSheetDialog.dismiss()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_mysteries, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.itemId

        if (id == R.id.share) {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, textToShare)
            intent.type = "text/plain"

            startActivity(Intent.createChooser(intent, "Share to:"))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolBar() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolBarDimisteri)
        setSupportActionBar(toolbar)

        val actionBar = supportActionBar

        actionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar.setCustomView(R.layout.action_bar_mysteries)

        val textView = findViewById<TextView>(R.id.tvTitle)
        val textViewDays = findViewById<TextView>(R.id.txtDays)

        val days = Utility.getDaysArray(this)
        val mysteries = Utility.getMysteriesArray(this)

        when (intent.getStringExtra("name")) {
            mysteries!![0] -> { textViewDays.text = days!![0]}
            mysteries[1] -> { textViewDays.text = days!![1]}
            mysteries[2] -> { textViewDays.text = days!![2]}
            mysteries[3] -> { textViewDays.text = days!![3]}
        }

        textView.text = intent.getStringExtra("name")

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.back_button)
    }

    private fun populateMystery (str: String) {
        val number = Utility.getNumbersArray(this)
        val txtHeading = findViewById<TextView>(R.id.txtNameMisteri)
        val txtDisplayMystery = findViewById<TextView>(R.id.txtDisplayMystery)
        txtDisplayMystery.movementMethod = ScrollingMovementMethod()

        textToShare = ""

        for (x in readDatabase())
        {
            if (intent.getStringExtra("name") == x.mysteryName) {
                when (str) {
                    number!![0] -> {
                        textToShare = x.firstMystery
                        txtDisplayMystery.text = x.firstMystery
                    }
                    number[1] -> {
                        textToShare = x.secondMystery
                        txtDisplayMystery.text = x.secondMystery
                    }
                    number[2] -> {
                        textToShare = x.thirdMystery
                        txtDisplayMystery.text = x.thirdMystery
                    }
                    number[3] -> {
                        textToShare = x.fourthMystery
                        txtDisplayMystery.text = x.fourthMystery
                    }
                    number[4] -> {
                        textToShare = x.fifthMystery
                        txtDisplayMystery.text = x.fifthMystery
                    }
                }
            }
        }

        textToShare = str + "\r\n\r\n" + textToShare

        val content = SpannableString(str)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        txtHeading.text = content

        txtHeading.text = str
    }

    private fun readDatabase () : MutableList<Mystery> {
        val db = DataBaseHandler(this)
        var data: MutableList<Mystery> = ArrayList()

        when (Utility.getLanguage(this)) {
            0 -> {data = db.getMysteryData("Sesotho")}
            1 -> {data = db.getMysteryData("IsiZulu")}
            2 -> {data = db.getMysteryData("Setswana")}
            3 -> {data = db.getMysteryData("IsiXhosa")}
            4 -> {data = db.getMysteryData("English")}
        }
        return data
    }
}
