package com.baller.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller.model.Standing
import com.baller.repository.StandingsRepository
import kotlinx.coroutines.launch

class StandingsViewModel(
    private val repository: StandingsRepository
) : ViewModel() {

    private val TAG = "StandingsViewModel"

    private val _standings = MutableLiveData<List<Standing>>()
    val standings: LiveData<List<Standing>> = _standings

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    // Our custom league data class
    data class League_Data(
        val name: String,
        val seasonId: Int
    )

    val leagues = listOf(
        League_Data("Danish Superliga", 23584),
        League_Data("Scottish Premier League", 23690)
    )

    private val _currentLeague = MutableLiveData<League_Data>()
    val currentLeague: LiveData<League_Data> = _currentLeague

    init {
        // default league
        _currentLeague.value = leagues[0]
        fetchStandings(_currentLeague.value!!.seasonId)
    }

    fun setCurrentLeague(league: League_Data) {
        _currentLeague.value = league
        fetchStandings(league.seasonId)
    }

    fun fetchStandings(seasonId: Int) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val data = repository.getStandingsBySeasonId(seasonId)
                _standings.value = data
                _error.value = ""
            } catch (e: Exception) {
                _standings.value = emptyList()
                _error.value = "Error: ${e.message}"
                Log.e(TAG, "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}
