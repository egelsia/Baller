package com.baller.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.baller.R
import com.baller.adapter.StandingsAdapter
import com.baller.databinding.FragmentStandingsBinding
import com.baller.model.Team
import com.baller.repository.StandingsRepository
import com.baller.viewmodel.StandingsViewModel

class StandingsFragment : Fragment() {

    // View binding
    private var _binding: FragmentStandingsBinding? = null
    private val binding get() = _binding!!

    // ViewModel
    private lateinit var standingsViewModel: StandingsViewModel

    // Recycler adapter
    private lateinit var standingsAdapter: StandingsAdapter

    // Custom factory so we can pass the repository into the VM constructor
    inner class StandingsViewModelFactory(
        private val repository: StandingsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StandingsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StandingsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create the repository + ViewModel
        val repository = StandingsRepository(requireContext())
        standingsViewModel = ViewModelProvider(
            this,
            StandingsViewModelFactory(repository)
        )[StandingsViewModel::class.java]

        // Prepare the adapter and assign the onClick action
        standingsAdapter = StandingsAdapter(standingsViewModel) { team ->
            navigateToTeamDetails(team)
        }

        // Setup RecyclerView
        binding.recyclerViewStandings.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = standingsAdapter
        }

        // Setup league toggle group
        binding.leagueToggleGroup.apply {
            check(R.id.buttonDanishSuperliga) // Default selection
            addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.buttonDanishSuperliga -> {
                            standingsViewModel.setCurrentLeague(standingsViewModel.leagues[0])
                        }
                        R.id.buttonScottishPremierLeague -> {
                            standingsViewModel.setCurrentLeague(standingsViewModel.leagues[1])
                        }
                    }
                }
            }
        }

        // Observe the ViewModel
        observeViewModel()

        // Pull-to-refresh
        val swipeRefreshLayout = binding.swipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            standingsViewModel.currentLeague.value?.let { league ->
                // Re-fetch standings for current league
                standingsViewModel.fetchStandings(league.seasonId)
            }
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observeViewModel() {
        // Standings data
        standingsViewModel.standings.observe(viewLifecycleOwner) { standings ->
            if (standings.isNotEmpty()) {
                standingsAdapter.submitList(standings)
                binding.textViewErrorStandings.visibility = View.GONE
            } else {
                binding.textViewErrorStandings.visibility = View.VISIBLE
                binding.textViewErrorStandings.text = "No standings data available."
            }
        }

        // Loading
        standingsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarStandings.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Error
        standingsViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                binding.textViewErrorStandings.visibility = View.VISIBLE
                binding.textViewErrorStandings.text = errorMessage
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            } else {
                binding.textViewErrorStandings.visibility = View.GONE
            }
        }
    }

    private fun navigateToTeamDetails(team: Team) {
        // Example of using Navigation Component
        val directions = StandingsFragmentDirections.actionStandingsFragmentToTeamDetailsActivity(
            teamId = team.id,
            teamName = team.name,
            teamFounded = team.founded?.toString() ?: "No Data",
            teamShort = team.short_code ?: "No Data",
            teamImgPath = team.image_path,
            teamActiveSeasonId = standingsViewModel.currentLeague.value?.seasonId ?: -1
        )
        findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
