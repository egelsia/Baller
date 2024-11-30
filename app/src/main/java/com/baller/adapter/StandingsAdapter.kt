package com.baller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baller.R
import com.baller.databinding.StandingsItemBinding
import com.baller.model.Standing
import com.baller.model.Team
import com.baller.viewmodel.StandingsViewModel

class StandingsAdapter(private val standingsViewModel: StandingsViewModel, private val onClick: (Team) -> Unit) : ListAdapter<Standing, StandingsAdapter.StandingsViewHolder>(StandingsDiffCallback()) {

    class StandingsViewHolder private constructor(
        private val binding: StandingsItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(standing: Standing, standingsViewModel: StandingsViewModel, onClick: (Team) -> Unit) {
            binding.standing = standing
            binding.viewModel = standingsViewModel

            binding.root.setOnClickListener {
                onClick(standing.participant)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): StandingsViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StandingsItemBinding.inflate(layoutInflater, parent, false)
                return StandingsViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StandingsViewHolder {
        return StandingsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StandingsViewHolder, position: Int) {
        holder.bind(getItem(position), standingsViewModel, onClick)
    }

    class StandingsDiffCallback: DiffUtil.ItemCallback<Standing>() {
        override fun areItemsTheSame(oldItem: Standing, newItem: Standing): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Standing, newItem: Standing): Boolean {
            return oldItem == newItem
        }
    }



}
