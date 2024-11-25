package com.baller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.baller.model.Standing
import com.bumptech.glide.Glide

//class StandingsAdapter: RecyclerView.Adapter<StandingsAdapter.StandingViewHolder>() {
//    private var standings: List<Standing> = emptyList()
//
//    fun updateStandings(newStandings: List<Standing>) {
//        standings = newStandings
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): StandingViewHolder {
//        val binding = ItemStandingBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return StandingViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = standings.size
//
//
//    override fun onBindViewHolder(holder: StandingViewHolder, position: Int) {
//        holder.bind(standings[position])
//    }
//
//    class StandingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private lateinit var standing: Standing
//
//
//    }
//}