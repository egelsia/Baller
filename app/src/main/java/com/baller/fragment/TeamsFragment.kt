package com.baller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.baller.adapter.TeamAdapter
import com.baller.databinding.FragmentTeamsBinding
import com.baller.repository.TeamsRepository
import com.baller.viewmodel.TeamsViewModel

class TeamsFragment : Fragment() {

    private lateinit var teamViewModel: TeamsViewModel
    private var _binding: FragmentTeamsBinding? = null
    private val binding get() = _binding!!

    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = TeamsRepository(requireContext())
        teamViewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(TeamsViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return TeamsViewModel(repository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        )[TeamsViewModel::class.java]
        teamViewModel.initLeagues(requireContext())
        val adapter = TeamAdapter(teamViewModel) { team ->
            val directions = TeamsFragmentDirections.actionTeamsFragmentToTeamDetailsActivity(
                teamId = team.id,
                teamName = team.name,
                teamFounded = team.founded?.toString() ?: "No Data",
                teamShort = team.short_code ?: "No Data",
                teamImgPath = team.image_path,
                teamActiveSeasonId = team.activeseasons?.firstOrNull()?.id ?: -1
            )
            findNavController().navigate(directions)
        }

        binding.recyclerView.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        swipeRefresh = binding.swipeRefreshLayout
        swipeRefresh.setOnRefreshListener {
            teamViewModel.fetchTeams()
        }

        teamViewModel.teams.observe(viewLifecycleOwner) { teams ->
            adapter.submitList(teams)
            swipeRefresh.isRefreshing = false
        }

        teamViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (!isLoading) {
                swipeRefresh.isRefreshing = false
            }
        }

        teamViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                if(errorMessage != "") {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }

        teamViewModel.fetchTeams()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
