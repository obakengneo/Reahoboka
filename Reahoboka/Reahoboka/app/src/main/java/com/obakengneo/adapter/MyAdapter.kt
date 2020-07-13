package com.obakengneo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.obakengneo.R
import com.obakengneo.model.Model

class MyAdapter (var mCtx: Context, var resources:Int, var items:List<Model>):ArrayAdapter<Model> (mCtx, resources, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater:LayoutInflater = LayoutInflater.from(mCtx)
        val view = layoutInflater.inflate(resources, null)

        val titleTextView:TextView = view.findViewById(R.id.txtTextName)
        val index:TextView = view.findViewById(R.id.txtIndex)
        val uncategorized:TextView = view.findViewById(R.id.txtUncategorized)
        val txtAlign = view.findViewById<TextView>(R.id.txtAlign)

        val mItem: Model = items[position]

        uncategorized.text = mItem.language
        txtAlign.text = mItem.indexedValue
        index.text = mItem.indexedValue
        titleTextView.text = mItem.title

        return view
    }
}