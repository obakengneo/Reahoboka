package com.obakengneo.presentation

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.obakengneo.adapter.MyViewPagerAdapter
import com.obakengneo.R

class FragmentFavourites : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View = inflater.inflate(R.layout.favourites_fragment, container, false)
        val viewPager:ViewPager = view.findViewById(R.id.viewPager)
        val tabs:TabLayout = view.findViewById(R.id.tabs)
        val hymn = this.resources.getStringArray(R.array.hymn).toMutableList()
        val prayers = this.resources.getStringArray(R.array.prayer).toMutableList()
        val adapter = MyViewPagerAdapter(childFragmentManager)


        adapter.addFragment(FragmentHymn(), hymn[getSelectedLanguage()])
        adapter.addFragment(FragmentPrayer(), prayers[getSelectedLanguage()])

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

        super.onCreate(savedInstanceState)
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

    private fun setUpToolBar () {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        val hymn = this.resources.getStringArray(R.array.hymn).toMutableList()

            when (getFilteredLanguage()) {
                0 -> {actionBar!!.title = "Favourites (Sesotho)"}
                1 -> {actionBar!!.title = "Favourites (IsiZulu)"}
                2 -> {actionBar!!.title = "Favourites (Setswana)"}
                3 -> {actionBar!!.title = "Favourites (IsiXhosa)"}
                4 -> {actionBar!!.title = "Favourites (English)"}
                else -> {actionBar!!.title = "Favourites (All)"}
            }

        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer)
    }

    private fun getSelectedLanguage () : Int {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        var language = 0

        if (sharedPreferences.contains("Language")) {
            language = sharedPreferences.getInt("Language", 0)
        }
        return language
    }

    private fun getFilteredLanguage () : Int {
        val sharedPref: SharedPreferences = this.requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)

        return sharedPref.getInt("Filter", getSelectedLanguage())
    }

    private fun setFilteredLanguage (int: Int) {
        val sharedPref: SharedPreferences = this.requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()

        editor.putInt("Filter", int)
        editor.apply()
    }
}