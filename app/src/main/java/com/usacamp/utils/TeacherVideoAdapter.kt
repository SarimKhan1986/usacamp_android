package com.usacamp.utils
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.usacamp.R
import com.usacamp.activities.VideoActivity
import com.usacamp.model.MediaItem

import java.util.*

class TeacherVideoAdapter(private var moviesList: ArrayList<MediaItem>?) :
        RecyclerView.Adapter<TeacherVideoAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: RoundedImageView = view.findViewById(R.id.teacheritem)
        var v = view
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_teacher_video, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = moviesList!![position]
        val uri = Uri.parse(movie.mstrImage)
        Picasso.with(holder.v.context).load(uri).placeholder(R.drawable.banner01_3x).into(holder.image)

        holder.image.setOnClickListener{
            val videoIntent = Intent(holder.itemView.getContext(), VideoActivity::class.java)
            videoIntent.putExtra("video_path", movie.mstrVideoFile)
            holder.itemView.getContext().startActivity(videoIntent)

        }
    }
    override fun getItemCount(): Int {
        return moviesList!!.size
    }
}