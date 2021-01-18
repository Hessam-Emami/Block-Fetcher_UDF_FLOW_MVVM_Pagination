package com.emami.blockfetcher.common.extensions

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.emami.blockfetcher.R
import com.google.android.material.color.MaterialColors


fun ImageView.src(iconPath: String?) {
    val circularProgressDrawable = CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 30f
        setColorSchemeColors(MaterialColors.getColor(this@src, R.attr.colorOnPrimary))
        start()
    }
    Glide.with(this).load(iconPath).placeholder(circularProgressDrawable).into(this)
}