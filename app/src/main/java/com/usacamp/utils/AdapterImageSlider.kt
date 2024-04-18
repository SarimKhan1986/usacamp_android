package com.usacamp.utils;
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.usacamp.R
import com.usacamp.activities.BannerDetail
import com.usacamp.model.ImageObj
import java.util.*


class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var image = v.findViewById<View>(R.id.image) as ImageView

}
class AdapterImageSlider(private var imglist: ArrayList<ImageObj>?, val layout: Int) :
        RecyclerView.Adapter<MyViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val o: ImageObj = imglist!!.get(position)
        val uri = Uri.parse(o.mstrImageLink)
        Picasso.with(holder.itemView.getContext()).load(uri).placeholder(R.drawable.newtopbanner).into(holder.image)
        holder.image.setOnClickListener{
            val detailInt = Intent(holder.itemView.getContext(), BannerDetail::class.java)
            detailInt.putExtra("bannerlink", o.mstrLink)
            detailInt.putExtra("backgroundcolor", o.mstrTitleBackground)
            detailInt.putExtra("fontcolor", o.mstrFontColor)
            holder.itemView.getContext().startActivity(detailInt)
        }
    }
    override fun getItemCount(): Int {
        return imglist!!.size
    }
}