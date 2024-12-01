package com.baller.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.baller.R
import com.baller.utils.LeagueUtils
import com.bumptech.glide.Glide

class FixtureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fixture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val leagues = LeagueUtils(requireContext()).getLeagues()

        val danishLeague = leagues[0]
        val scottishLeague = leagues[1]

        view.findViewById<TextView>(R.id.league1_name).text = danishLeague.name
        view.findViewById<TextView>(R.id.league2_name).text = scottishLeague.name

        val danishImg = view.findViewById<ImageView>(R.id.league1_img)
        val scottishImg = view.findViewById<ImageView>(R.id.league2_img)

        Glide.with(view.context)
            .load(danishLeague.image_path)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .fitCenter()
            .into(danishImg)

        Glide.with(view.context)
            .load(scottishLeague.image_path)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .fitCenter()
            .into(scottishImg)
    }

}