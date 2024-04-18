package com.usacamp.utils
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

import com.usacamp.R

import java.util.*

class AboutLevelAdapter(private var itemList: ArrayList<aboutlevelitem>?) :
    RecyclerView.Adapter<AboutLevelAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var text1: TextView = view.findViewById(R.id.abouttitle)
        var text2: TextView = view.findViewById(R.id.aboutdetail)
        var v = view
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.aboutlevelitem, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.text1.setText(itemList!!.get(position)?.leveltitel)
        holder.text2.setText(itemList!!.get(position)?.leveldetail)
    }
    override fun getItemCount(): Int {
        return itemList!!.size
    }
}