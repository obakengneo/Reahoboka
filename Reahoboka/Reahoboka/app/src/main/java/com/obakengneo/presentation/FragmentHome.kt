package com.obakengneo.presentation

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.obakengneo.R
import com.obakengneo.util.Utility

class FragmentHome : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.home_fragment, container, false)
        val scriptureList = this.resources.getStringArray(R.array.scripture).toMutableList()
        val scripture = view.findViewById<TextView>(R.id.scriptureTxt)
        scripture.movementMethod = ScrollingMovementMethod()

        scripture.text = scriptureList[Utility.getLanguage(this.requireActivity())]
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setUpToolBar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_languages, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.itemLanguage) {
            Utility.navigateToActivity(this.requireActivity(), this.requireContext(), ActivityAbout::class.java)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpToolBar () {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        val language = when (Utility.getLanguage(this.requireActivity())) {
            0 -> { " (Sesotho)" }
            1 -> { " (IsiZulu)" }
            2 -> { " (Setswana)" }
            3 -> { " (IsiXhosa)" }
            4 -> { " (English)" }
            else -> { " (English)" }
        }

        actionBar!!.title = "Home$language"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer)
    }
}