package com.baller.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.baller.R
import com.baller.adapter.FixturesAdapter
import com.baller.viewmodel.FixturesViewModel
import com.bumptech.glide.Glide

class TeamDetailsActivity : AppCompatActivity() {
    private val viewModel: FixturesViewModel by viewModels()
    private lateinit var fixturesAdapter: FixturesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_team_details)

        val args = TeamDetailsActivityArgs.fromBundle(intent.extras!!)

        setupViews(args)
        setupRecyclerView()
        observeViewModel()

        // Fetch fixtures for the team
        viewModel.fetchTeamFixture(args.teamActiveSeasonId, args.teamId)
    }

    private fun setupViews(args: TeamDetailsActivityArgs) {
        findViewById<TextView>(R.id.team_name).text = args.teamName
        findViewById<TextView>(R.id.shortCode).text = args.teamShort
        findViewById<TextView>(R.id.foundationDate).text = args.teamFounded

        Glide.with(this)
            .load(args.teamImgPath)
            .into(findViewById<ImageView>(R.id.imageView))
    }

    private fun setupRecyclerView() {
        fixturesAdapter = FixturesAdapter()
        findViewById<RecyclerView>(R.id.fixturesRecyclerView).apply {
            adapter = fixturesAdapter
            layoutManager = LinearLayoutManager(this@TeamDetailsActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.fixtures.observe(this) { fixtures ->
            fixturesAdapter.submitList(fixtures)
        }

        viewModel.loading.observe(this) { isLoading ->
            findViewById<ProgressBar>(R.id.loadingProgressBar).visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}