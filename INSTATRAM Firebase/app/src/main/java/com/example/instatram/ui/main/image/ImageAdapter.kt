package com.example.instatram.ui.main.image

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instatram.R
import com.example.instatram.data.Tram
import com.example.instatram.ui.main.home.OnTramItemClickListner
import kotlinx.android.synthetic.main.image_grid.view.*

class ImageAdapter(
        val urls: List<String>,val names:List<String>,val dossier: String,var clickListnerr: OnImageItemClickListner
): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    inner class ImageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override  fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ImageViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.image_grid, parent, false)
        return ImageViewHolder(view)

    }

    override fun getItemCount(): Int {
        return urls.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = urls[position]
        val namess=names[position]
        val nameclick=namess.toString()

        Glide.with(holder.itemView).load(url).into(holder.itemView.ivImage)
        holder.itemView.imagename.text=(namess)
        holder.itemView.setOnLongClickListener{
            clickListnerr.clickedd(nameclick,dossier)
        }

    }
    companion object {

    }
}
interface OnImageItemClickListner{
    fun clickedd(name:String,dossier:String):Boolean{
        return true
    }
}