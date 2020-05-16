package com.example.android.architecture.blueprints.movies.data.source.remote.retrofit

import com.example.android.architecture.blueprints.movies.data.source.remote.response.KeyResponse
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

private const val API_KEY_HEADER_NAME = "api-key"

class ApikeyInterceptor @Inject constructor() : Interceptor {

    private var apiKey: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentApiKey = apiKey
        val request = chain.request()

        if (isNewKeyRequest(request)) {
            val response = chain.proceed(request)
            processNewApiKey(response)
            return response
        }

        return if (currentApiKey != null) {
            val newRequest = request.newBuilder().addHeader(API_KEY_HEADER_NAME, currentApiKey).build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(request)
        }

    }

    private fun processNewApiKey(response: Response) {
        val responseBody = response.peekBody(Long.MAX_VALUE)
        if (response.isSuccessful) {
            try {
                val keyResponse = Gson().fromJson(responseBody.string(), KeyResponse::class.java)
                apiKey = keyResponse.key
                Timber.v("The new API KEY was configured successfully: $apiKey")
            } catch (e: JsonSyntaxException) {
                Timber.e(e, "Api Key response couldn't be processed")
            }
        }
    }

    private fun isNewKeyRequest(request: Request): Boolean {
        return request.url().pathSegments().last() == API_KEY_REQUEST_PATH_SEGMENT
    }
}
