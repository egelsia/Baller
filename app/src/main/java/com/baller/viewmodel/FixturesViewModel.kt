package com.baller.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller.model.Fixture
import com.baller.service.APIService
import com.baller.service.ApiResult
import kotlinx.coroutines.launch

class FixturesViewModel: ViewModel() {
    private val apiService = APIService()
    private val TAG = "FixturesViewModel"

    private val _fixtures = MutableLiveData<List<Fixture>>()
    val fixtures: LiveData<List<Fixture>> = _fixtures

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun fetchTeamFixture(seasonId: Int, teamId: Int) {
        viewModelScope.launch {
            _loading.value = true

            when (val result = apiService.getTeamSchedules(seasonId, teamId)) {
                is ApiResult.Success -> {
                    _fixtures.value = result.data
                    _error.value = ""
                }
                is ApiResult.Error -> {
                    _fixtures.value = emptyList()
                    _error.value = result.message
                    Log.e(TAG, "Error ${result.code}: ${result.message}")
                }
            }

            _loading.value = false
        }
    }
}