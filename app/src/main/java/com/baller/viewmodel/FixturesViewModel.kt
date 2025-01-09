package com.baller.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baller.model.Fixture
import com.baller.repository.FixturesRepository
import kotlinx.coroutines.launch

class FixturesViewModel(
    private val repository: FixturesRepository
) : ViewModel() {

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
            try {
                val data = repository.getTeamSchedules(seasonId, teamId)
                _fixtures.value = data
                _error.value = ""
            } catch (e: Exception) {
                _fixtures.value = emptyList()
                _error.value = "Error: ${e.message}"
                Log.e(TAG, "Error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}
