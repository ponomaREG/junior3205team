package com.team3205.junior.ui.util

import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(url: String){
    Glide.with(this)
        .load(url)
        .into(this)
}