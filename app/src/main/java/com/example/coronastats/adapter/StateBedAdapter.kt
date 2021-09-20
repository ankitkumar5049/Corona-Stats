package com.example.coronastats.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coronastats.R
import com.example.coronastats.model.BedRegional

class StateBedAdapter(val context: Context,private val bedList:ArrayList<BedRegional>):RecyclerView.Adapter<StateBedAdapter.BedViewHolder>() {
    class BedViewHolder(view: View): RecyclerView.ViewHolder(view){
        val stateName : TextView = view.findViewById(R.id.txtStateName)
        val totalBed : TextView = view.findViewById(R.id.txtTotalBed)
        val urbanBed : TextView = view.findViewById(R.id.txtUrbanBed)
        val ruralBed : TextView = view.findViewById(R.id.txtRuralBed)
        val totHos : TextView = view.findViewById(R.id.txtTotalHos)
        val urbanHos : TextView = view.findViewById(R.id.txtUrbanHos)
        val ruralHos : TextView = view.findViewById(R.id.txtRuralHos)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StateBedAdapter.BedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_bed_row,parent,false)
        return BedViewHolder(view)
    }

    override fun onBindViewHolder(holder: StateBedAdapter.BedViewHolder, position: Int) {
        val reg = bedList[position]
        holder.stateName.text = reg.state
        holder.ruralBed.text = reg.rBed.toString()
        holder.ruralHos.text = reg.rHos.toString()
        holder.totHos.text = reg.totHos.toString()
        holder.urbanHos.text = reg.uHos.toString()
        holder.urbanBed.text = reg.uBed.toString()
        holder.totalBed.text = reg.totBed.toString()
    }

    override fun getItemCount(): Int {
       return bedList.size
    }
}