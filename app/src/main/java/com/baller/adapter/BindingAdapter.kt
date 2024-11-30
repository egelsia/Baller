package com.baller.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.baller.R
import com.baller.model.Team
import com.baller.viewmodel.TeamsViewModel
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    url?.let {
        Glide.with(view.context)
            .load(it)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .fitCenter()
            .into(view)
    }
}

@BindingAdapter("viewModel", "team")
fun setLeagueName(textView: TextView, viewModel: TeamsViewModel, team: Team) {
    textView.text = viewModel.getLeagueName(team)
}
@BindingAdapter("movementIndicator")
fun setMovementIndicator(imageView: ImageView, result: String?) {
    result?.let {
        when (it) {
            "up" -> {
                imageView.setImageResource(R.drawable.ic_arrow_up_green)
            }
            "down" -> {
                imageView.setImageResource(R.drawable.ic_arrow_down_red)
            }
            "equal" -> {
                imageView.setImageResource(R.drawable.ic_horizontal_black)
            }
            else -> {
                imageView.setImageResource(0)
            }
        }
    }
}