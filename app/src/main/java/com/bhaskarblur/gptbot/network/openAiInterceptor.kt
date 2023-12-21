package com.bhaskarblur.gptbot.network

import com.bhaskarblur.gptbot.R
import okhttp3.Interceptor
import okhttp3.Response

class OpenAiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();
        val authenticatedRequest = request.newBuilder()
            .addHeader("Authorization", "Bearer ${R.string.openAIKey}")
            .build();

        return chain.proceed(authenticatedRequest);
    }
}