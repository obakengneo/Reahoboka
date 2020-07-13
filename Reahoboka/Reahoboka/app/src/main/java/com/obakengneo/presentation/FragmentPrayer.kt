package com.obakengneo.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obakengneo.R
import com.obakengneo.adapter.MyAdapter
import com.obakengneo.db.DataBaseHandler
import com.obakengneo.model.Model
import com.obakengneo.model.Prayer
import com.obakengneo.util.Utility
import kotlinx.android.synthetic.main.add_alert_dialog.view.*
import kotlinx.android.synthetic.main.bottom_favourites.view.*
import kotlinx.android.synthetic.main.show_toast.view.*


class FragmentPrayer : Fragment() {
    lateinit var listView: ListView
    var listWithUnfilteredElements: MutableList<Model> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View = inflater.inflate(R.layout.prayer_fragment, container, false)

        listView = view.findViewById(R.id.lstDithapelo)
        setUpFragment()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchView:SearchView

        if (Utility.getFragment(this.requireActivity()) == 1) {
            inflater.inflate(R.menu.menu_favourites, menu)
            searchView = menu.findItem(R.id.searchIconDifela).actionView as SearchView

            searchHandler(searchView)
        } else {
            inflater.inflate(R.menu.menu_hymn, menu)

            searchView = menu.findItem(R.id.searchIconDifela).actionView as SearchView
            searchHandler(searchView)
        }

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (id == R.id.searchIconDifela) {
            return true
        }

        if (item.itemId == R.id.filterList) {
            setFilteredLanguage()

            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val prayer = this.resources.getStringArray(R.array.prayer).toMutableList()

        if (Utility.getFragment(this.requireActivity()) == 0) {
            Utility.setUpToolBar(this.requireActivity(), prayer[Utility.getLanguage(this.requireActivity())])
        } else {
            Utility.setUpToolBar(this.requireActivity(), "Favourites")
        }
    }

    private fun setUpFragment() {
        val sharedPref = this.requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val data = readDatabase()
        var filterList: MutableList<Prayer> = ArrayList()
        var list = mutableListOf<Model>()

        for (x in data) {
            if (Utility.getFragment(this.requireActivity()) == 0) {
                list = setAdapter(list, x)
                filterList = data
            } else {
                if (sharedPref.contains(x.name + "Int")) {
                    if (sharedPref.getBoolean(x.name + "Int", true)) {
                        list = setAdapter(list, x)
                        filterList.add(x)
                    }
                }
            }
        }

        listWithUnfilteredElements = list
        setDataToListView(list, filterList)
    }

    private fun setAdapter (list:MutableList<Model>, prayerObject:Prayer) :MutableList<Model>{
        when (Utility.getLanguage(this.requireActivity())) {
            0 -> {list.add(Model(prayerObject.name, prayerObject.id.toString() + ".  ",prayerObject.language))}
            1 -> {list.add(Model(prayerObject.name, prayerObject.id.toString() + ".  ",prayerObject.language))}
            2 -> {list.add(Model(prayerObject.name, prayerObject.id.toString() + ".  ",prayerObject.language))}
            3 -> {list.add(Model(prayerObject.name, prayerObject.id.toString() + ".  ",prayerObject.language))}
            4 -> {list.add(Model(prayerObject.name, prayerObject.id.toString() + ".  ",prayerObject.language))}
        }
        return list
    }

    private fun setDataToListView (originalList:MutableList<Model>, updatedList:MutableList<Prayer>) {
        listView.adapter = MyAdapter(
            this.requireContext(),
            R.layout.row,
            originalList
        )

        listView.setOnItemClickListener { _:AdapterView<*>, _:View, position:Int, _:Long ->
            Utility.navigateToAnotherActivity(this.requireActivity(), this.requireContext(),
                updatedList[position].id.toString(), updatedList[position].name, updatedList[position].prayer) }

        listView.onItemLongClickListener =
            AdapterView.OnItemLongClickListener { _, _, position, _ ->
                val sharePrayer = updatedList[position].name+"\r\n\r\n"+ updatedList[position].prayer

                alertBoxPopUp(updatedList[position].name, sharePrayer)

                true
            }
    }

    private fun readDatabase () :MutableList<Prayer> {
        val db = DataBaseHandler(this.requireContext())
        var data: MutableList<Prayer> = ArrayList()

        if (Utility.getFilteredLanguage(this.requireActivity()) == 0 || Utility.getFragment(this.requireActivity()) == 0) {
            when (Utility.getLanguage(this.requireActivity())) {
                0 -> { data = db.getPrayerData("Sesotho") }
                1 -> { data = db.getPrayerData("IsiZulu") }
                2 -> { data = db.getPrayerData("Setswana") }
                3 -> { data = db.getPrayerData("IsiXhosa") }
                4 -> { data = db.getPrayerData("English") }
            }

            return data
        }

        return db.getPrayerData("")
    }

    private fun searchHandler (searchView:SearchView) {
        val sharedPref = requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val data = readDatabase()
        val thisActivity = this.requireActivity()
        val refreshedPrayers = mutableListOf<Prayer>()

        searchView.findViewById<SearchView>(androidx.appcompat.R.id.search_plate)
        searchView.setBackgroundColor(Utility.getColor((R.attr.colorAccent), requireContext()))

        val searchPlate = searchView.findViewById<SearchView>(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Search..."
        searchPlate.setHintTextColor(Utility.getColor((R.attr.colorPrimary), requireContext()))
        searchPlate.setTextColor(Utility.getColor((R.attr.colorPrimaryDark1), requireContext()))

        val searchClose = searchView.findViewById<SearchView>(androidx.appcompat.R.id.search_close_btn) as ImageView
        searchClose.setColorFilter(Utility.getColor((R.attr.colorPrimary), requireContext()))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                var list = mutableListOf<Model>()


                if (newText.isEmpty()) {
                    setDataToListView(listWithUnfilteredElements, data)
                }
                else {
                    for (i  in data) {
                        val name =  i.name
                        val searchString = i.id.toString() + i.name
                        if (Utility.getFragment(thisActivity) == 0) {
                            if(searchString.contains(newText, ignoreCase = true)) {
                                list = setAdapter(list,i)
                                refreshedPrayers.add(i)
                            }
                        } else {
                            if(sharedPref.contains(name+"Int")) {
                                if (sharedPref.getBoolean(name + "Int", true)) {
                                    if (searchString.contains(newText, ignoreCase = true)) {
                                        list = setAdapter(list, i)
                                        refreshedPrayers.add(i)
                                    }
                                }
                            }
                        }
                    }
                    setDataToListView(list, refreshedPrayers)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    private fun alertBoxPopUp (name: String, shareString:String) : Boolean {
        val view: View = layoutInflater.inflate(R.layout.add_alert_dialog, null)
        val sharedPref: SharedPreferences = this.requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())
        val preference = sharedPref.getBoolean(name + "Int", false)
        var number = 0

        if (Utility.getFragment(this.requireActivity()) == 1) {
            view.imgFavourites.setImageResource(R.drawable.heart1)
            view.addToFavourites.text = "Remove from Favourites"
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        view.addToFavourites.setOnClickListener {
            try {
                if (preference) {
                    if (Utility.getFragment(this.requireActivity()) == 0) {
                        Utility.displayToast(
                            "'${name}' already added to favourites.",
                            this.requireContext(),
                            "Short",
                            this.requireActivity()
                        )
                    } else {
                        Utility.displayToast(
                            "'${name}' removed from favourites.",
                            this.requireContext(),
                            "Short",
                            this.requireActivity()
                        )
                        editor.remove(name)
                        editor.putBoolean(name + "Int", false)
                        editor.apply()
                        setUpFragment()
                        number = 1
                    }
                } else {
                    Utility.displayToast(
                        "'${name}' added to favourites.",
                        this.requireContext(),
                        "Short",
                        this.requireActivity()
                    )
                    editor.putString(name, name)
                    editor.putBoolean(name + "Int", true)
                    editor.apply()
                }
            } finally {
                bottomSheetDialog.dismiss()
            }
        }

        if (number == 1) { return false }

        view.shareTo.setOnClickListener {
            view.shareTo.setOnClickListener {
                val intent = Intent()

                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, shareString)
                intent.type = "text/plain"

                startActivity(Intent.createChooser(intent, "Share to:"))
            }
        }

        return true
    }

    private fun setFilteredLanguage () : Boolean {
        val sharedPref: SharedPreferences = this.requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPref.edit()
        val view: View = layoutInflater.inflate(R.layout.bottom_favourites, null)
        val bottomSheetDialog = BottomSheetDialog(this.requireContext())

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        view.selectedLanguage.setOnClickListener {
            editor.putInt("FilteredLanguage", 0)
            editor.apply()
            setUpFragment()
            bottomSheetDialog.dismiss()
        }

        view.showAllFavourites.setOnClickListener {
            editor.putInt("FilteredLanguage", 1)
            editor.apply()
            setUpFragment()
            bottomSheetDialog.dismiss()
        }

        return true
    }
}