package com.baller.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baller.R
import com.baller.model.Fixture

class FixturesAdapter : RecyclerView.Adapter<FixturesAdapter.FixtureViewHolder>() {
    private var fixtures: List<Fixture> = emptyList()

    class FixtureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val scoreText: TextView = itemView.findViewById(R.id.scoreText)

        fun bind(fixture: Fixture) {
            scoreText.text = fixture.getFormattedScore()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fixture, parent, false)
        return FixtureViewHolder(view)
    }

    override fun onBindViewHolder(holder: FixtureViewHolder, position: Int) {
        holder.bind(fixtures[position])
    }

    override fun getItemCount() = fixtures.size

    fun submitList(newFixtures: List<Fixture>) {
        fixtures = newFixtures
        notifyDataSetChanged()
    }
}