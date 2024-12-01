package com.baller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.baller.adapter.TeamAdapter
import com.baller.databinding.FragmentTeamsBinding
import com.baller.viewmodel.TeamsViewModel

class TeamsFragment : Fragment() {
    private lateinit var teamViewModel: TeamsViewModel
    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        teamViewModel = ViewModelProvider(this)[TeamsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamViewModel.initLeagues(requireContext())
        val adapter = TeamAdapter(teamViewModel) { team ->
                val directions = TeamsFragmentDirections.actionTeamsFragmentToTeamDetailsActivity(
                    teamId = team.id,
                    teamName = team.name,
                    teamFounded = team.founded?.toString() ?: "No Data",
                    teamShort = team.short_code ?: "No Data",
                    teamImgPath = team.image_path
                )
            findNavController().navigate(directions)
        }
        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        teamViewModel.teams.observe(viewLifecycleOwner) { teams ->
            adapter.submitList(teams)
        }

        teamViewModel.loading.observe(viewLifecycleOwner) {isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        teamViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }

        teamViewModel.fetchTeams()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}