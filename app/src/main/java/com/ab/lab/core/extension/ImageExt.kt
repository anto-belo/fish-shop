package com.ab.lab.core.extension

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.ab.lab.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

/**
 * Загружает изображение по ссылке imageReference и передаёт для показа в imageView
 */
fun ImageView.showExt(
    imageReference: String,
    onResourceReady: (() -> Unit)? = null,
    onLoadFailed: (() -> Unit)? = null
) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    @Suppress("DEPRECATION")
    circularProgressDrawable.setColorFilter(Color.parseColor("#F57C00"), PorterDuff.Mode.SRC_IN)
    circularProgressDrawable.start()

    val requestOptions = RequestOptions()
        //.downsample(DownsampleStrategy.CENTER_INSIDE)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.ic_cloud_off_24)
        .fitCenter()

    val listener = object : RequestListener<Drawable> {
        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onResourceReady?.invoke()
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onLoadFailed?.invoke()
            return false
        }
    }

    Glide.with(this.context)
        .load(imageReference)
        .apply(requestOptions)
        .listener(listener)
        // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}
