package com.baller.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.baller.R
import com.bumptech.glide.Glide

class TeamDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_team_details)

        val args = TeamDetailsActivityArgs.fromBundle(intent.extras!!)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.team_name).text = args.teamName
        findViewById<TextView>(R.id.shortCode).text = args.teamShort
        findViewById<TextView>(R.id.foundationDate).text = args.teamFounded
        val imageView = findViewById<ImageView>(R.id.imageView)

        Glide.with(this)
            .load(args.teamImgPath)
            .into(imageView)
    }
}