package com.baller.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller.model.Team
import com.baller.service.APIService
import com.baller.service.ApiResult
import kotlinx.coroutines.launch

class TeamsViewModel: ViewModel() {

    private val apiService = APIService()
    private val TAG = "TeamsViewModel"

    private val _teams = MutableLiveData<List<Team>>()
    val teams: LiveData<List<Team>> = _teams

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchTeams() {
        viewModelScope.launch {

            _loading.value = true

            when (val result = apiService.getAllTeams()) {
                is ApiResult.Success -> {
                    _teams.value = result.data
                }
                is ApiResult.Error -> {
                    _teams.value = emptyList()
                    _error.value = result.message
                    Log.w(TAG, "${result.code}: ${result.message}")
                }
            }
        }
    }
}