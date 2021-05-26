package com.example.instatram.ui.main.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import androidx.recyclerview.widget.RecyclerView
import com.example.instatram.R
import com.example.instatram.data.Tram

class MainRecyclerAdapter(val context: Context, var clickListner: OnTramItemClickListner,
                          val trams: List<Tram>): RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {
    override fun getItemCount() = trams.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    val view = inflater.inflate(R.layout.tram_grid, parent, false)
    return ViewHolder(view)
}
    override fun onBindViewHolder(holder: ViewHolder, position: Int)  {
    val trame = trams[position]
    with(holder) {
        buttonn?.let {
            it.text = trame.name
            it.setOnClickListener{
                clickListner.onItemClick(trame,position)
            }

        }


    }
    }
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val buttonn: Button = itemView.findViewById<Button>(R.id.buttonn)
    }
}
interface OnTramItemClickListner{
    fun onItemClick(item: Tram, position: Int)
}