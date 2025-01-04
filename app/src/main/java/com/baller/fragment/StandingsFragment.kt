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
import com.baller.R
import com.baller.adapter.StandingsAdapter
import com.baller.databinding.FragmentStandingsBinding
import com.baller.viewmodel.StandingsViewModel

class StandingsFragment : Fragment() {
    private lateinit var standingsViewModel: StandingsViewModel
    private var _binding: FragmentStandingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var standingsAdapter: StandingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStandingsBinding.inflate(inflater, container, false)
        standingsViewModel = ViewModelProvider(this)[StandingsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        standingsViewModel.setCurrentLeague(standingsViewModel.leagues[0])
        setupRecyclerView()
        setupLeagueSelection()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        standingsAdapter = StandingsAdapter(standingsViewModel) { team ->
            standingsViewModel.currentLeague.value?.let { currentLeague ->
                val directions = StandingsFragmentDirections.actionStandingsFragmentToTeamDetailsActivity(
                    teamId = team.id,
                    teamName = team.name,
                    teamFounded = team.founded?.toString() ?: "No Data",
                    teamShort = team.short_code ?: "No Data",
                    teamImgPath = team.image_path,
                    teamActiveSeasonId = currentLeague.seasonId // Use current league's seasonId
                )
                findNavController().navigate(directions)
            }
        }

        binding.recyclerViewStandings.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = standingsAdapter
        }
    }

    private fun setupLeagueSelection() {
        binding.leagueToggleGroup.apply {
            check(R.id.buttonDanishSuperliga) // Set default selection

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
    }

    private fun observeViewModel() {
        // Observe standings data
        standingsViewModel.standings.observe(viewLifecycleOwner) { standings ->
            if (standings.isNotEmpty()) {
                standingsAdapter.submitList(standings)
                binding.textViewErrorStandings.visibility = View.GONE
            } else {
                binding.textViewErrorStandings.visibility = View.VISIBLE
                binding.textViewErrorStandings.text = "No standings data available."
            }
        }

        // Observe loading state
        standingsViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarStandings.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe error state
        standingsViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null && errorMessage.isNotEmpty()) {
                binding.textViewErrorStandings.visibility = View.VISIBLE
                binding.textViewErrorStandings.text = errorMessage
                Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            } else {
                binding.textViewErrorStandings.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}