package com.baller.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.baller.databinding.TeamCardBinding
import com.baller.model.Team
import com.baller.viewmodel.TeamsViewModel

class TeamAdapter(private val teamViewModel: TeamsViewModel,
    private val onClick: (Team) -> Unit) : ListAdapter<Team, TeamAdapter.TeamViewHolder>(TeamDiffCallback()) {

    class TeamViewHolder private constructor(
        private val binding: TeamCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team, teamViewModel: TeamsViewModel, onClick: (Team) -> Unit) {
            binding.team = team
            binding.viewModel = teamViewModel

            binding.root.setOnClickListener {
                onClick(team)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TeamViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TeamCardBinding.inflate(layoutInflater, parent, false)
                return TeamViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        holder.bind(getItem(position), teamViewModel, onClick)
    }

    class TeamDiffCallback: DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }
    }
}