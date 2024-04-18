package com.usacamp.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.usacamp.R
import com.usacamp.activities.MyApplication
import com.usacamp.activities.VideoActivity
import com.usacamp.model.MediaItem
import java.util.*
import kotlin.math.roundToInt


class HomeAdapterVideo(private var moviesList: ArrayList<MediaItem>?, val type: Int) : // type : 0 Main , 1 : Small
        RecyclerView.Adapter<HomeAdapterVideo.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.img_main_video)
        var txtTitle :TextView = view.findViewById(R.id.text_main_title)
        var txtDec : TextView = view.findViewById(R.id.text_main_desc)
        var playBtn : Button = view.findViewById(R.id.btn_play)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_slider_video_small, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val movie = moviesList!![position]
        val uri = Uri.parse(movie.mstrImage)
        Picasso.with(holder.itemView.context).load(uri).placeholder(R.drawable.banner01_3x).into(holder.image)
//        DownloadImageFromInternet(holder.image).execute(movie.mstrImage)
        holder.txtTitle.setText(movie.mstrTitle)
        holder.txtDec.setText(movie.mstrDesc)
        holder.image.setOnClickListener{
            val videoIntent = Intent(holder.itemView.getContext(), VideoActivity::class.java)
            videoIntent.putExtra("video_path", movie.mstrVideoFile)
            holder.itemView.getContext().startActivity(videoIntent)

        }

        when (type) {
            0 -> holder.playBtn.visibility = View.VISIBLE
            1 -> {
                holder.playBtn.visibility = View.VISIBLE
                var params:LinearLayout.LayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT)
                params.width = (MyApplication.getInstance().getPhoneWidth() / 2.5f).roundToInt();
                holder.itemView.setLayoutParams(params);
            }
        }
//        holder.setIsRecyclable (false);
    }
    override fun getItemCount(): Int {
        return moviesList!!.size
    }

    @SuppressLint("StaticFieldLeak")
    @Suppress("DEPRECATION")
    private inner class DownloadImageFromInternet(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>() {
//        init {
//            Toast.makeText(applicationContext, "Please wait, it may take a few minute...",     Toast.LENGTH_SHORT).show()
//        }
        override fun doInBackground(vararg urls: String): Bitmap? {
            Log.d("imageDownload", urls.toString())
            val imageURL = urls[0]
            var image: Bitmap? = null
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
            }
            catch (e: Exception) {
                Log.e("Error Message", e.message.toString())
                e.printStackTrace()
            }
            return image
        }
        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }
}