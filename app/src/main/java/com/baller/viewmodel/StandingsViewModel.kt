package com.baller.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller.model.Standing
import com.baller.service.APIService
import com.baller.service.ApiResult
import kotlinx.coroutines.launch

class StandingsViewModel: ViewModel() {
    private val apiService = APIService()
    private val TAG = "StandingsViewModel"

    private val _standings = MutableLiveData<List<Standing>>()
    val standings: LiveData<List<Standing>> = _standings

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchStandings(seasonId: Int) {
        viewModelScope.launch {
            _loading.value = true

            when (val result = apiService.getStandingsBySeasonId(seasonId)) {
                is ApiResult.Success -> {
                    _standings.value = result.data
                    Log.d(TAG, result.data.toString())
                    _error.value = ""
                }
                is ApiResult.Error -> {
                    _standings.value = emptyList()
                    _error.value = result.message
                    Log.e(TAG, "Error ${result.code}: ${result.message}")
                }
            }

            _loading.value = false
        }
    }

    private val _currentLeague = MutableLiveData<League>()
    val currentLeague: LiveData<League> = _currentLeague

    data class League(
        val name: String,
        val seasonId: Int
    )

    val leagues = listOf(
        League("Danish Superliga", 23584),
        League("Scottish Premier League", 23690)
    )

    init {
        _currentLeague.value = leagues[0]
    }

    fun setCurrentLeague(league: League) {
        _currentLeague.value = league
        fetchStandings(league.seasonId)
    }
}