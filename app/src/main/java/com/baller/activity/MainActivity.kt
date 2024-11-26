package com.baller.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.baller.R
import com.baller.databinding.ActivityMainBinding
import com.baller.fragment.FixtureFragment
import com.baller.fragment.StandingsFragment
import com.baller.fragment.TeamsFragment
import com.baller.viewmodel.StandingsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        replaceFragment(FixtureFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.teams -> replaceFragment(TeamsFragment())
                R.id.fixtures -> replaceFragment(FixtureFragment())
                R.id.standings -> replaceFragment(StandingsFragment())
                else -> {}
            }
            true
        }
        val standings = StandingsViewModel()
        standings.fetchStandings(23584)

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    // Danish Superliga season id = 23584
    // Scottish Premiership season id = 23690
}