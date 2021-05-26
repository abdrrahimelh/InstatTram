@file:Suppress("DEPRECATION")

package com.example.instatram.ui.main.home
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.instatram.R
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.RecyclerView
import com.example.instatram.data.Tram
import com.example.instatram.ui.main.PageViewModel
import com.example.instatram.ui.main.image.image_activity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Home : Fragment(),
    OnTramItemClickListner {
    private lateinit var viewModel: PageViewModel

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        viewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)

        viewModel.TramData.observe(this, Observer
        {
            val adapter = MainRecyclerAdapter(
                requireContext(),
                this,
                it
            )
            recyclerView.adapter = adapter

            
        })
        return view
    }
    override fun onItemClick(item: Tram, position: Int) {
        val intent = Intent(context,image_activity::class.java)
        intent.putExtra("id", item.id)
        intent.putExtra("name",item.name)
        intent.putExtra("lat",item.lat)
        intent.putExtra("lon",item.lon)


        startActivity(intent)

    }

    companion object {

        @JvmStatic
        fun newInstance(

        ) =
            Home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}