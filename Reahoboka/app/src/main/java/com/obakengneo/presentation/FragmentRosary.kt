package com.obakengneo.presentation

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obakengneo.R
import com.obakengneo.util.Utility
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*

class FragmentRosary : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.mystery_fragment, container, false)

        populateView(view)
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val rosary = this.resources.getStringArray(R.array.rosary).toMutableList()
        Utility.setUpToolBar(this.requireActivity(), rosary[Utility.getLanguage(this.requireActivity())])
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_rosary, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.dimisteri) {
            showBottomNavigationBar()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun populateView (view:View) {
        val number = Utility.getNumbersArray(this.requireActivity())

        view.findViewById<TextView>(R.id.mysteryNameHeading).text = getRosaryGuideData("Heading")
        view.findViewById<TextView>(R.id.mysteryTop).text = getRosaryGuideData("Top")

        view.findViewById<TextView>(R.id.mysteryMiddle1).text = getRosaryGuideData("Middle")
        view.findViewById<TextView>(R.id.mysteryMiddle2).text = getRosaryGuideData("Middle")
        view.findViewById<TextView>(R.id.mysteryMiddle3).text = getRosaryGuideData("Middle")
        view.findViewById<TextView>(R.id.mysteryMiddle4).text = getRosaryGuideData("Middle")
        view.findViewById<TextView>(R.id.mysteryMiddle5).text = getRosaryGuideData("Middle")

        view.findViewById<TextView>(R.id.mysteriName1).text = number!![0]
        view.findViewById<TextView>(R.id.mysteriName2).text = number[1]
        view.findViewById<TextView>(R.id.mysteriName3).text = number[2]
        view.findViewById<TextView>(R.id.mysteriName4).text = number[3]
        view.findViewById<TextView>(R.id.mysteriName5).text = number[4]
    }

    private fun showBottomNavigationBar() {
        val mystery = Utility.getMysteriesArray(this.requireActivity())
        val view1: View = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())

        view1.findViewById<TextView>(R.id.mysterySorrow).text = mystery!![0]
        view1.findViewById<TextView>(R.id.mysteryLight).text = mystery[1]
        view1.findViewById<TextView>(R.id.mysteryJoy).text = mystery[2]
        view1.findViewById<TextView>(R.id.mysteryGlory).text = mystery[3]

        bottomSheetDialog.setContentView(view1)
        bottomSheetDialog.show()

        view1.mahlokoSheet.setOnClickListener {
            Utility.navigateToActivityWithData("name", mystery[0], this.requireActivity(), this.requireContext(), ActivityDisplayMystery::class.java)
            bottomSheetDialog.dismiss() }

        view1.leseliSheet.setOnClickListener {
            Utility.navigateToActivityWithData("name", mystery[1], this.requireActivity(), this.requireContext(), ActivityDisplayMystery::class.java)
            bottomSheetDialog.dismiss() }

        view1.thaboSheet.setOnClickListener {
            Utility.navigateToActivityWithData("name", mystery[2], this.requireActivity(), this.requireContext(), ActivityDisplayMystery::class.java)
            bottomSheetDialog.dismiss() }

        view1.letlotloSheet.setOnClickListener {
            Utility.navigateToActivityWithData("name", mystery[3], this.requireActivity(), this.requireContext(), ActivityDisplayMystery::class.java)
            bottomSheetDialog.dismiss() }
    }

    private fun getRosaryGuideData (input:String) : String? {
        return when (input) {
            "Heading" -> {this.requireContext().resources.getStringArray(R.array.mysteriesGuideHeading)[Utility.getLanguage(this.requireActivity())].toString()}
            "Middle" -> {this.requireContext().resources.getStringArray(R.array.mysteriesDescriptionMiddle)[Utility.getLanguage(this.requireActivity())].toString()}
            "Top" -> {this.requireContext().resources.getStringArray(R.array.mysteriesTop)[Utility.getLanguage(this.requireActivity())].toString()}
            else -> null
        }
    }
}