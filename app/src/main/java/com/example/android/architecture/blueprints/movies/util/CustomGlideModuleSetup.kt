package com.example.android.architecture.blueprints.movies.util

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.Excludes
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpLibraryGlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.android.architecture.blueprints.movies.MyApplication
import com.example.android.architecture.blueprints.movies.R
import java.io.InputStream

@GlideModule
@Excludes(OkHttpLibraryGlideModule::class)
class CustomGlideModuleSetup: AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val options = RequestOptions()
                .placeholder(R.drawable.movie_placeholder) // for loading or error state
                .centerCrop()
        builder.setDefaultRequestOptions(options)
        super.applyOptions(context, builder)
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)

        val okHttpClient = (context.applicationContext as MyApplication).component.okHttpClient

        val factory = OkHttpUrlLoader.Factory(okHttpClient)
        registry.replace(
                GlideUrl::class.java,
                InputStream::class.java,
                factory
        )
    }
}