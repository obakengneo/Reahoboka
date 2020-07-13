package com.obakengneo.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.obakengneo.R
import com.obakengneo.adapter.MyAdapter
import com.obakengneo.db.DataBaseHandler
import com.obakengneo.model.Model
import com.obakengneo.model.Hymn
import com.obakengneo.util.Utility
import kotlinx.android.synthetic.main.add_alert_dialog.view.*
import kotlinx.android.synthetic.main.bottom_favourites.view.*


class FragmentHymn : Fragment() {
    lateinit var listView: ListView
    var listWithUnfilteredElements: MutableList<Model> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view:View = inflater.inflate(R.layout.hymn_fragment, container, false)

        listView = view.findViewById<ListView>(R.id.lstDifela)
        setUpFragment()
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val searchView:SearchView

        when {
            Utility.getFragment(this.requireActivity()) == 1 -> {
                inflater.inflate(R.menu.menu_favourites, menu)
                searchView = menu.findItem(R.id.searchIconDifela).actionView as SearchView
                searchHandler(searchView)
            }
            else -> {
                inflater.inflate(R.menu.menu_hymn, menu)
                searchView = menu.findItem(R.id.searchIconDifela).actionView as SearchView
                searchHandler(searchView)
            }
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
        val hymn = this.resources.getStringArray(R.array.hymn).toMutableList()

        if (Utility.getFragment(this.requireActivity()) == 0) {
            Utility.setUpToolBar(this.requireActivity(), hymn[Utility.getLanguage(this.requireActivity())])
        } else {
            Utility.setUpToolBar(this.requireActivity(), "Favourites")
        }
    }

    private fun setUpFragment() {
        val sharedPref = this.requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        var filterList: MutableList<Hymn> = ArrayList()
        var list = mutableListOf<Model>()

        for (x in readDatabase()) {
            if (Utility.getFragment(this.requireActivity()) == 0) {
                list = setAdapter(list, x)
                filterList = readDatabase()
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

    private fun setAdapter (list:MutableList<Model>, hymnObject:Hymn) :MutableList<Model>{
        when (Utility.getLanguage(this.requireActivity())) {
            0 -> {list.add(Model(hymnObject.name, hymnObject.id.toString() + ".  ",checkForUncategorized(hymnObject.language)))}
            1 -> {list.add(Model(hymnObject.name, hymnObject.id.toString() + ".  ",checkForUncategorized(hymnObject.language)))}
            2 -> {list.add(Model(hymnObject.name, hymnObject.id.toString() + ".  ",checkForUncategorized(hymnObject.language)))}
            3 -> {list.add(Model(hymnObject.name, hymnObject.id.toString() + ".  ",checkForUncategorized(hymnObject.language)))}
            4 -> {list.add(Model(hymnObject.name, hymnObject.id.toString() + ".  ",checkForUncategorized(hymnObject.language)))}
        }
        return list
    }

    private fun checkForUncategorized (str:String) : String {
         when(str) {
            "IsiXhosa" -> {return "IsiXhosa (Uncategorized)"}
             "IsiZulu" -> {return "IsiZulu(Uncategorized)"}
             "Setswana" -> {return "Setswana (Uncategorized)"}
        }
        return  str
    }

    private fun setDataToListView (originalList:MutableList<Model>, updatedList:MutableList<Hymn>) {
        listView.adapter = MyAdapter(
            this.requireContext(),
            R.layout.row,
            originalList
        )

        listView.setOnItemClickListener { _:AdapterView<*>, _:View, position:Int, _:Long ->
        Utility.navigateToAnotherActivity(this.requireActivity(), this.requireContext(),
            updatedList[position].id.toString(), updatedList[position].name, updatedList[position].hymn)}

        listView.onItemLongClickListener =
            OnItemLongClickListener { _, _, position, _ ->
                val shareHymn = updatedList[position].name+"\r\n\r\n"+ updatedList[position].hymn

                alertBoxPopUp(updatedList[position].name, shareHymn)
                true }
    }

    private fun readDatabase () :MutableList<Hymn> {
        val db = DataBaseHandler(this.requireContext())
        var data: MutableList<Hymn> = ArrayList()

        if (Utility.getFilteredLanguage(this.requireActivity()) == 0 || Utility.getFragment(this.requireActivity()) == 0) {
            when (Utility.getLanguage(this.requireActivity())) {
                0 -> { data = db.getHymnData("Sesotho") }
                1 -> { data = db.getHymnData("IsiZulu") }
                2 -> { data = db.getHymnData("Setswana") }
                3 -> { data = db.getHymnData("IsiXhosa") }
                4 -> { data = db.getHymnData("English") }
            }
            return data
        }

        return db.getHymnData("")
    }

    private fun searchHandler (searchView:SearchView) {
        val sharedPref = requireActivity().getSharedPreferences("com.obakengneo", Context.MODE_PRIVATE)
        val thisActivity = this.requireActivity()

        searchView.findViewById<SearchView>(androidx.appcompat.R.id.search_plate)
        searchView.setBackgroundColor(Utility.getColor((R.attr.colorAccent), this.requireContext()))

        val searchPlate = searchView.findViewById<SearchView>(androidx.appcompat.R.id.search_src_text) as EditText
        searchPlate.hint = "Search..."
        searchPlate.setHintTextColor(Utility.getColor((R.attr.colorPrimary), requireContext()))
        searchPlate.setTextColor(Utility.getColor((R.attr.colorPrimaryDark1), requireContext()))

        val searchClose = searchView.findViewById<SearchView>(androidx.appcompat.R.id.search_close_btn) as ImageView
        searchClose.setColorFilter(Utility.getColor((R.attr.colorPrimary), requireContext()))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                var list = mutableListOf<Model>()
                val refreshedHymns = mutableListOf<Hymn>()

                if (newText.isEmpty()) {
                    setDataToListView(listWithUnfilteredElements, readDatabase())
                }
                else {
                    for (i  in readDatabase()) {
                        val name =  i.name
                        val searchString = i.id.toString() + i.name
                        if (Utility.getFragment(thisActivity) == 0){
                            if(searchString.contains(newText, ignoreCase = true)) {
                                list = setAdapter(list,i)
                                refreshedHymns.add(i)
                            }
                        } else {
                            if(sharedPref.contains(name+"Int")) {
                                if (sharedPref.getBoolean(name + "Int", true)) {
                                    if (searchString.contains(newText, ignoreCase = true)) {
                                        list = setAdapter(list, i)
                                        refreshedHymns.add(i)
                                    }
                                }
                            }
                        }
                    }
                    setDataToListView(list, refreshedHymns)
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